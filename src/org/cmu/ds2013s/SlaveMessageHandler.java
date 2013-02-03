package org.cmu.ds2013s;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * handler of commands
 */
public class SlaveMessageHandler extends MessageHandler {
  private static final Log logger = LogFactory.getLog(SlaveMessageHandler.class);

  private SlaveManager _context;

  public SlaveMessageHandler(Socket s, ManagerContext context) {
    super(s);
    this._context = (SlaveManager) context;
  }

  @Override
  public void handle() {
    Command command = this.getCommand();
    switch (command.getType()) {
      case MIGRATE_SOURCE:
        if (ProcessManager.DEBUG) {
          logger.info("Slave received command MIGRATE_SOURCE");
        }
        handleSource(command);
        break;
      case MIGRATE_SEND:
        if (ProcessManager.DEBUG) {
          logger.info("Slave received command MIGRATE_SEND");
        }
        handleSend(command);
        break;
      case NEW_JOB:
        if (ProcessManager.DEBUG) {
          logger.info("Slave received command NEW_JOB");
        }
        handleNewJob(command);
        break;
      default:
        break;
    }
  }
  /**
   * handle Source command (from master): 
   * select one process from currently running process list
   * serialize the process and send the process to another slave indicated in the command
   * */
  public void handleSource(Command command) {
    MigrateSourceCommand msc = (MigrateSourceCommand) command;
    Thread toMgr = null;
    MigratableProcess mp = null;
    /* get one process to migrate */
    synchronized (_context.processes) {
      if (_context.processes.size() == 0)
        return;
      toMgr = _context.processes.keySet().iterator().next();
      mp = _context.processes.get(toMgr);
      _context.processes.remove(toMgr);
    }
    /* if got one alive process, migrate it */
    if (toMgr != null && toMgr.isAlive()) {
      /* suspend the process */
      mp.suspend();
      /* serialize the process */
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutput out = null;
      byte[] object = null;
      try {
        out = new ObjectOutputStream(bos);
        out.writeObject(mp);
        object = bos.toByteArray();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          out.close();
          bos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      int jobid = Integer.parseInt(toMgr.getName().substring(0, toMgr.getName().indexOf(" ")));
      MigrateSendCommand toSend = new MigrateSendCommand(_context.get_hostname(),
              _context.get_port(), jobid, object);
      if (ProcessManager.DEBUG) {
        System.out.println(Arrays.toString(toSend.toBytes()));
      }
      /* migrate the process to another slave */
      try {
        CommunicationUtil.sendCommand(msc.getHost(), msc.getPort(), toSend.toBytes());
      } catch (ConnectException e) {
        handleSend(toSend); // if failed, send to self
      }
    }
  }
  /**
   * handle Send command (from another slave):
   * this command consists of one process migrated from another slave
   * deserialize the process object and restart it
   * */
  public void handleSend(Command command) {
    MigrateSendCommand msc = (MigrateSendCommand) command;
    int jobid = msc.getJobId();
    byte[] object = msc.getObject();
    /* deserialize the object */
    ByteArrayInputStream in = new ByteArrayInputStream(object);
    MigratableProcess mp = null;
    Thread thread = null;
    ObjectInputStream is;
    
    try {
      is = new ObjectInputStream(in);
      mp = (MigratableProcess) is.readObject();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    logger.info(mp.toString());
    thread = new Thread(mp);
    thread.setName(jobid+" "+mp.toString());
    synchronized (_context.processes) {
      _context.processes.put(thread, mp);
      thread.start();
    }
  }
  /**
   * handle NewJob command (from master):
   * start a process of the class indicated in the command
   * */
  public void handleNewJob(Command command) {
    NewJobCommand njc = (NewJobCommand) command;
    String cmd = njc.getInput();
    int jobid = njc.getJobId();
    /* parse the class name and arguments */
    int spaceIndex = cmd.indexOf(' ');
    String className = "";
    String[] arguments = null;

    if (spaceIndex == -1) {
      className = cmd;
    } else {
      className = cmd.substring(0, spaceIndex);
      arguments = cmd.substring(spaceIndex + 1).split(" ");
    }
    Object[] objargs = { arguments };
    Class theClass = null;
    /* start the process */
    try {
      logger.info("CLASSNAME: "+className);
      theClass = Class.forName(className);
      MigratableProcess process = (MigratableProcess) theClass.getConstructor(String[].class)
              .newInstance(objargs);
      Thread thread = new Thread(process);
      thread.setName(jobid+" "+process.toString());
      _context.processes.put(thread, process);
      thread.start();
      logger.info("JOB: "+cmd+" STARTED!");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }
}

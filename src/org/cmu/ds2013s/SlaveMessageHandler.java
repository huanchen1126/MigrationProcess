package org.cmu.ds2013s;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmu.ds2013s.Command.CommandType;

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
        logger.info("Slave received command MIGRATE_SOURCE");
        handleSource(command);
        break;
      case MIGRATE_SEND:
        logger.info("Slave received command MIGRATE_SEND");
        handleSend(command);
        break;
      case NEW_JOB:
        logger.info("Slave received command NEW_JOB");
        handleNewJob(command);
        break;
      default:
        break;
    }
  }

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
      mp.suspend();
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
      MigrateSendCommand toSend = new MigrateSendCommand(_context.get_hostname(),
              _context.get_port(), object);
      System.out.println(Arrays.toString(toSend.toBytes()));
      CommunicationUtil.sendCommand(msc.getHost(), msc.getPort(), toSend.toBytes());
    }
  }

  public void handleSend(Command command) {
    MigrateSendCommand msc = (MigrateSendCommand) command;
    byte[] object = msc.get_object();
    /* deserialize the object */
    ByteArrayInputStream in = new ByteArrayInputStream(object);
    MigratableProcess mp = null;
    Thread thread = null;
    ObjectInputStream is;
    try {
      is = new ObjectInputStream(in);
      mp = (MigratableProcess) is.readObject();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    thread = new Thread(mp);
    synchronized (_context.processes) {
      _context.processes.put(thread, mp);
      thread.start();
    }
  }

  public void handleNewJob(Command command) {
    NewJobCommand njc = (NewJobCommand) command;
    //logger.info("MESSAGE: "+Arrays.toString(njc.toBytes()));
    String cmd = njc.get_input();
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
    try {
      logger.info("CLASSNAME: "+className);
      theClass = Class.forName(className);
      MigratableProcess process = (MigratableProcess) theClass.getConstructor(String[].class)
              .newInstance(objargs);
      Thread thread = new Thread(process);
      thread.setName(_context.getId()+" "+process.toString());
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

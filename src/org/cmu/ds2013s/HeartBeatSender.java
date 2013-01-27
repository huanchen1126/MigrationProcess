package org.cmu.ds2013s;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cmu.ds2013s.Command.CommandType;

public class HeartBeatSender implements Runnable {
  private static final Log logger = LogFactory.getLog(HeartBeatSender.class);
  SlaveManager slaveManager;
  int currentLoad = 0;
  public final static int HEART_BEAT_PERIOD = 5;
  

  public HeartBeatSender(SlaveManager slaveManager) {
    this.slaveManager = slaveManager;
  }

  @Override
  public void run() {
    ScheduledExecutorService schExec = Executors.newScheduledThreadPool(8);
    ScheduledFuture<?> schFuture = schExec.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        LinkedList<Thread> toDelete = new LinkedList<Thread>();
        for(Thread thread : slaveManager.processes){
          if(!thread.isAlive()){
            slaveManager.console.printf("Process \""+thread.getName()+"\" was terminated");
            toDelete.add(thread);
          }
        }
        for(Thread thread : toDelete){
          slaveManager.processes.remove(thread);
        }
        currentLoad = slaveManager.processes.size();
        /* send heart beat here */
        logger.info("heart beat sending ..."+currentLoad);
        HeartBeatCommand command = new HeartBeatCommand(CommandType.HEART_BEAT,currentLoad,slaveManager.get_hostname(),slaveManager.get_port());
        Socket socket = null;
        try {
          socket = new Socket(slaveManager.get_masterHostname(),slaveManager.get_port());
          OutputStream out = socket.getOutputStream();
          out.write(command.toBytes());
        } catch (UnknownHostException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        try {
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }, 0, HEART_BEAT_PERIOD, TimeUnit.SECONDS);
  }

}

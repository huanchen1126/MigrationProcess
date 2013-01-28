package org.cmu.ds2013s;

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

  public final static int HEART_BEAT_PERIOD = 2;

  public HeartBeatSender(SlaveManager slaveManager) {
    this.slaveManager = slaveManager;
  }

  @Override
  public void run() {
    ScheduledExecutorService schExec = Executors.newScheduledThreadPool(8);
    ScheduledFuture<?> schFuture = schExec.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        synchronized (slaveManager.processes) {
          LinkedList<Thread> toDelete = new LinkedList<Thread>();
          for (Thread thread : slaveManager.processes.keySet()) {
            if (!thread.isAlive()) {
              slaveManager.console.printf("Process \"" + thread.getName() + "\" was terminated");
              toDelete.add(thread);
            }
          }
          for (Thread thread : toDelete) {
            slaveManager.processes.remove(thread);
          }
          slaveManager.setCurrentLoad(slaveManager.processes.size());
        }
        /* send heart beat here */
        logger.info("heart beat sending ..." + slaveManager.getCurrentLoad());
        HeartBeatCommand command = new HeartBeatCommand(CommandType.HEART_BEAT, slaveManager
                .getCurrentLoad(), slaveManager.get_hostname(),
                slaveManager.get_port());
        CommunicationUtil.sendCommand(slaveManager.get_masterHostname(),
                slaveManager.get_masterPort(), command.toBytes());
      }
    }, 0, HEART_BEAT_PERIOD, TimeUnit.SECONDS);
  }
}

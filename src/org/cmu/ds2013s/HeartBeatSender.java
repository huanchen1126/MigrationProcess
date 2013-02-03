package org.cmu.ds2013s;

import java.net.ConnectException;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The heart beat sender sends information of currently running information from slave to master periodically
 * */
public class HeartBeatSender implements Runnable {
  private static final Log logger = LogFactory.getLog(HeartBeatSender.class);

  SlaveManager slaveManager;
  
  /* period between heart beat in seconds */
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
        String[] processlist = null;
        synchronized (slaveManager.processes) {
          LinkedList<Thread> toDelete = new LinkedList<Thread>();
          LinkedList<String> aliveProcessName = new LinkedList<String>();
          for (Thread thread : slaveManager.processes.keySet()) {
            if (!thread.isAlive()) {
              toDelete.add(thread);
            } else {
              aliveProcessName.add(thread.getName());
            }
          }
          /* remove terminated processes */
          for (Thread thread : toDelete) {
            slaveManager.processes.remove(thread);
          }
          /* update num of current running processes */
          slaveManager.setCurrentLoad(slaveManager.processes.size());
          /* build the process list to send to master */
          processlist = new String[aliveProcessName.size()];
          aliveProcessName.toArray(processlist);
        }
        /* send heart beat here */
        logger.info("heart beat sending ..." + slaveManager.getCurrentLoad());
        HeartBeatCommand command = new HeartBeatCommand(slaveManager.get_hostname(), slaveManager
                .get_port(), processlist);
        try {
          CommunicationUtil.sendCommand(slaveManager.get_masterHostname(),
                  slaveManager.get_masterPort(), command.toBytes());
        } catch (ConnectException e) {
          e.printStackTrace();
        }
      }
    }, 0, HEART_BEAT_PERIOD, TimeUnit.SECONDS);
  }
}

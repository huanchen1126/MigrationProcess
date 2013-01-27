package org.cmu.ds2013s;

import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartBeatSender implements Runnable {
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
      }
    }, 0, HEART_BEAT_PERIOD, TimeUnit.SECONDS);
  }

}

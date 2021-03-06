package org.cmu.ds2013s;

import org.apache.commons.logging.*;

import java.net.ConnectException;
import java.util.*;

public class LoadBalancer implements Runnable {
  private static final Log logger = LogFactory.getLog(LoadBalancer.class);

  // the reference to the master manager which use this load balancer
  private MasterManager _manager;

  // the strategy to do load balance
  private LoadBalancerStrategy _lbstrategy;

  public LoadBalancer(MasterManager manager) {
    this._manager = manager;
    this._lbstrategy = new DefaultLoadBalancerStrategy();
  }

  public void run() {
    if (ProcessManager.DEBUG) {
      logger.info("LoadBalancer run.");
    }

    this.doLoadBalance();
  }

  /**
   * Set another load balance strategy.
   * 
   * @param stra
   *          a specific load balance strategy to be set
   */
  public void setLoadBalancerStrategy(LoadBalancerStrategy stra) {
    this._lbstrategy = stra;
  }

  /**
   * Use load balance strategy to find migration tasks.
   * 
   * @return a list of migration tasks; if not need to migrate, return null;
   */
  private List<MigrationTask> getLoadBalanceTasks() {
    return this._lbstrategy.getLoadBalanceTasks(this._manager.getSlaves());
  }

  /**
   * execute load balance job
   * 
   */
  private void doLoadBalance() {
    List<MigrationTask> tasks = this.getLoadBalanceTasks();

    // nothing to do if the system is nearly balanced
    if (tasks == null || tasks.size() == 0) {
      if (ProcessManager.DEBUG) {
        logger.info("LoadBalancer to do nothing.");
      }
      return;
    }

    for (MigrationTask task : tasks) {
      if (ProcessManager.DEBUG) {
        System.out.println("migrate " + task.getNumber() + " tasks from "
                + task.getFrom().getPort() + " to " + task.getTo().getPort());
      }
      
      // for each migration task, generate and send a command to the targeted slave
      Command migcmd = new MigrateSourceCommand(task);
      String host = task.getFrom().getIp();
      int port = task.getFrom().getPort();
      
      try {
        CommunicationUtil.sendCommand(host, port, migcmd.toBytes());
      } catch (ConnectException e) {
        if (ProcessManager.DEBUG) {
          System.out.println("Send migration request failed.");
        }
      }
    }
  }

}

package org.cmu.ds2013s;

import org.apache.commons.logging.*;
import java.util.*;

public class LoadBalancer implements Runnable {
  private static final Log logger = LogFactory.getLog(LoadBalancer.class);

  private Vector<SlaveMeta> _slaves;

  private LoadBalancerStrategy _lbstrategy;

  public LoadBalancer(Vector<SlaveMeta> slaves) {
    this._slaves = slaves;
    this._lbstrategy = new DefaultLoadBalancerStrategy();
  }

  public void run() {
    logger.info("LoadBalancer run.");

    this.doAliveChecking();
    this.doLoadBalance();
  }

  /**
   * 
   * @param stra
   *          a specific load balance strategy to be set
   */
  public void setLoadBalancerStrategy(LoadBalancerStrategy stra) {
    this._lbstrategy = stra;
  }

  /**
   * Use load balance strategy to find migration tasks
   * 
   * @return a list of migration tasks; if not need to migrate, return null;
   */
  private List<MigrationTask> getLoadBalanceTasks() {
    return this._lbstrategy.getLoadBalanceTasks(this._slaves);
  }

  /**
   * remove all dead slaves
   */
  private void doAliveChecking() {
    synchronized (this._slaves) {
      List<SlaveMeta> tobedelete = new ArrayList<SlaveMeta>();
      
      for (SlaveMeta slave : this._slaves) {
        if (!slave.isAlive()) {
          tobedelete.add(slave);
        }
      }
      
      for(SlaveMeta slave : tobedelete) {
        logger.info("Slave " + slave.getIp() + ":" + slave.getPort() + " has been removed.");
        this._slaves.remove(slave);
      }
      
    }
  }

  private void doLoadBalance() {
    List<MigrationTask> tasks = this.getLoadBalanceTasks();

    // nothing to do
    if (tasks == null || tasks.size() == 0)
      return;

    for (MigrationTask task : tasks) {
      // do sth for each task
    }
  }

}

package org.cmu.ds2013s;

import org.apache.commons.logging.*;
import java.util.*;

public class LoadBalancer implements Runnable {
  private static final Log logger = LogFactory.getLog(LoadBalancer.class);
  
  private MasterManager _manager;

  private LoadBalancerStrategy _lbstrategy;

  public LoadBalancer(MasterManager manager) {
    this._manager = manager;
    this._lbstrategy = new DefaultLoadBalancerStrategy();
  }

  public void run() {
    logger.info("LoadBalancer run.");
    
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
    return this._lbstrategy.getLoadBalanceTasks(this._manager.getSlaves());
  }
  
  /**
   * execute load balance job
   * 
   */
  private void doLoadBalance() {
    List<MigrationTask> tasks = this.getLoadBalanceTasks();

    // nothing to do
    if (tasks == null || tasks.size() == 0) {
      logger.info("LoadBalancer to do nothing.");
      return;
    }

    for (MigrationTask task : tasks) {
      // TODO: do sth for each task
    }
  }

}

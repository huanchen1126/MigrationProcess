package org.cmu.ds2013s;

import org.apache.commons.logging.*;
import java.util.*;

public class LoadBalancer implements Runnable {
  private static final Log logger = LogFactory.getLog(LoadBalancer.class);

  private Map<String, SlaveMeta> _slaves; // key is HOST:PORT

  private LoadBalancerStrategy _lbstrategy;

  public LoadBalancer(Map<String, SlaveMeta> slaves) {
    this._slaves = slaves;
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
    return this._lbstrategy.getLoadBalanceTasks(this._slaves);
  }
  
  /**
   * execute load balance job
   * 
   */
  private void doLoadBalance() {
    List<MigrationTask> tasks = this.getLoadBalanceTasks();

    // nothing to do
    if (tasks == null || tasks.size() == 0)
      return;

    for (MigrationTask task : tasks) {
      // TODO: do sth for each task
    }
  }

}

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
    logger.info("LoadBalancer begin running.");
    
    this.doLoadBalance();
  }
  
  public void setLoadBalancerStrategy(LoadBalancerStrategy stra) {
    this._lbstrategy = stra;
  }
  
  private List<MigrationTask> getLoadBalanceTasks() {
    return this._lbstrategy.getLoadBalanceTasks(this._slaves);
  }
  
  private void doLoadBalance() {
    List<MigrationTask> tasks = this.getLoadBalanceTasks();
    
    // nothing to do
    if (tasks == null || tasks.size() == 0)
      return ;
    
    for(MigrationTask task : tasks) {
      // do sth for each task
    }
  }

}

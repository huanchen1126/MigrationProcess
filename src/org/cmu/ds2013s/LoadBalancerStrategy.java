package org.cmu.ds2013s;

import java.util.*;

/**
 * The load balancing strategy interface
 */
public interface LoadBalancerStrategy {
  public List<MigrationTask> getLoadBalanceTasks(List<SlaveMeta> slaves);
}

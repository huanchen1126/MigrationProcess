package org.cmu.ds2013s;

import java.util.*;

public interface LoadBalancerStrategy {
  public List<MigrationTask> getLoadBalanceTasks(List<SlaveMeta> slaves);
}

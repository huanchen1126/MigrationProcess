package org.cmu.ds2013s;

import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DefaultLoadBalancerStrategy implements LoadBalancerStrategy {

  @Override
  public List<MigrationTask> getLoadBalanceTasks(Map<String, SlaveMeta> slaves) {

    return null;
  }

}

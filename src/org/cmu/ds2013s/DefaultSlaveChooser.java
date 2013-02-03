package org.cmu.ds2013s;

import java.util.List;

/**
 * The class is the default to choose a slave node to assign the new
 * coming task. In this class, it just choose the slave with the lightest
 * workload.
 */
public class DefaultSlaveChooser implements SlaveChooserStrategy {

  @Override
  public SlaveMeta chooseSlave(List<SlaveMeta> slaves) {
    if (slaves.isEmpty())
      return null;
    else {
      SlaveMeta minwl = slaves.get(0);
      
      for(SlaveMeta sm : slaves) {
        if (sm.getWorkload() < minwl.getWorkload()) {
          minwl = sm;
        }
      }
      
      return minwl;
    }
  }

}

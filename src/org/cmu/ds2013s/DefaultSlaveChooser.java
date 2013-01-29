package org.cmu.ds2013s;

import java.util.List;

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

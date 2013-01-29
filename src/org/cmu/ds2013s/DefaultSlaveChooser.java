package org.cmu.ds2013s;

import java.util.List;

public class DefaultSlaveChooser implements SlaveChooserStrategy {

  @Override
  public SlaveMeta chooseSlave(List<SlaveMeta> slaves) {
    // TODO: temp code
    if (slaves.isEmpty())
      return null;
    else
      return slaves.get(0);
  }

}

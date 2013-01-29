package org.cmu.ds2013s;

import java.util.List;

public interface SlaveChooserStrategy {
  public SlaveMeta chooseSlave(List<SlaveMeta> slaves);
}

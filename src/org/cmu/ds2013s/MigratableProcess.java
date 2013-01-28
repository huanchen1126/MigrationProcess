package org.cmu.ds2013s;

import java.io.Serializable;

public interface MigratableProcess extends Runnable, Serializable {
  void suspend();
}

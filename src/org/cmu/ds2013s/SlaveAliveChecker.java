package org.cmu.ds2013s;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SlaveAliveChecker implements Runnable {
  private static final Log logger = LogFactory.getLog(SlaveAliveChecker.class);
  
  private MasterManager _manager;
  
  public SlaveAliveChecker(MasterManager manager) {
    this._manager = manager;
  }
  
  @Override
  public void run() {
    logger.info("SlaveAliveChecker run.");
    
    this.doAliveChecking();
  }
  
  /**
   * remove all dead slaves
   */
  private void doAliveChecking() {
    List<SlaveMeta> slaves = this._manager.getSlaves();

    if (slaves.size() == 0)
      return;

    List<SlaveMeta> tobedelete = new ArrayList<SlaveMeta>();

    for (SlaveMeta slave : slaves) {
      if (!slave.isAlive(System.currentTimeMillis())) {
        tobedelete.add(slave);
      }
    }

    for (SlaveMeta slave : tobedelete) {
      logger.info("Slave " + slave.getIp() + ":" + slave.getPort() + " has been removed.");
      this._manager.deleteSlaveMeta(SlaveMeta.getMapKey(slave));
    }
  }

}

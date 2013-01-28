package org.cmu.ds2013s;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SlaveAliveChecker implements Runnable {
  private static final Log logger = LogFactory.getLog(SlaveAliveChecker.class);
  
  private Map<String, SlaveMeta> _slaves; // key is HOST:PORT
  
  public SlaveAliveChecker(Map<String, SlaveMeta> slaves) {
    this._slaves = slaves;
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
    synchronized (this._slaves) {
      List<SlaveMeta> tobedelete = new ArrayList<SlaveMeta>();

      for (SlaveMeta slave : this._slaves.values()) {
        if (!slave.isAlive()) {
          tobedelete.add(slave);
        }
      }

      for (SlaveMeta slave : tobedelete) {
        logger.info("Slave " + slave.getIp() + ":" + slave.getPort() + " has been removed.");
        this._slaves.remove(slave);
      }
      
      // flip the alive status for each slave
      for (SlaveMeta slave : this._slaves.values()) {
        slave.setAlive(false);
      }

    }
  }


}

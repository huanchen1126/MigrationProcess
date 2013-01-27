package org.cmu.ds2013s;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MasterManager implements Runnable {
  private static final Log logger = LogFactory.getLog(MasterManager.class);

  private Vector<SlaveMeta> _slaves;

  public static final int LOAD_BALANCE_CYCLE_SEC = 1;

  private int _port;

  private String _ip;

  public MasterManager(int port) {
    this._port = port;
    this._ip = CommunicationUtil.getLocalIPAddress();

    this._slaves = new Vector<SlaveMeta>();
  }

  @Override
  public void run() {
    logger.info("MasterManager begin running on " + this._ip + ":" + this._port + ".");
    
    LoadBalancer loadbalancer = new LoadBalancer(this._slaves);
    ScheduledExecutorService loadBalSvr = Executors.newScheduledThreadPool(8);
    loadBalSvr.scheduleAtFixedRate(loadbalancer, 0, LOAD_BALANCE_CYCLE_SEC, TimeUnit.SECONDS);
  }
  
  /**
   * 
   * @return a copy of the status of all slaves
   */
  public List<SlaveMeta> getSlaves() {
    // create a copy of slave lists to prevent modification
    return new ArrayList<SlaveMeta>(this._slaves);
  }
  
 
}

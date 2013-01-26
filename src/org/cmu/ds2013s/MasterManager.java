package org.cmu.ds2013s;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MasterManager implements Runnable {
  
  private static final Logger logger = Logger.getLogger(MasterManager.class.getName());

  private List<SlaveMeta> _slaves;

  public static final int LOAD_BALANCE_CYCLE_SEC = 5;

  private int _port;

  private String _ip;

  public MasterManager(int port) {
    this._port = port;
    this._ip = CommunicationUtil.getLocalIPAddress();

    this._slaves = new Vector<SlaveMeta>();
  }

  @Override
  public void run() {
    logger.info("MasterManager begin run.");
    
    LoadBalancer loadbalancer = new LoadBalancer();
    
    ScheduledExecutorService loadBalSvr = Executors.newScheduledThreadPool(8);
    loadBalSvr.scheduleAtFixedRate(loadbalancer, 0, LOAD_BALANCE_CYCLE_SEC, TimeUnit.SECONDS);
  }

}

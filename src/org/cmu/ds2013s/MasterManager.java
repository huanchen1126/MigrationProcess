package org.cmu.ds2013s;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MasterManager implements ManagerContext {
  private static final Log logger = LogFactory.getLog(MasterManager.class);

  private Map<String, SlaveMeta> _slaves; // key is HOST:PORT

  public static final int LOAD_BALANCE_CYCLE_SEC = 5;

  private int _port;

  private String _ip;

  public MasterManager(int port) {
    this._port = port;
    this._ip = CommunicationUtil.getLocalIPAddress();

    this._slaves = Collections.synchronizedMap(new TreeMap<String, SlaveMeta>());
  }

  @Override
  public void run() {
    logger.info("MasterManager begin running on " + this._ip + ":" + this._port + ".");

    // start network listener
    NetworkListener listener = new NetworkListener(MasterMessageHandler.class, this._port, this);
    (new Thread(listener)).start();
    
    // start load balancer
    LoadBalancer loadbalancer = new LoadBalancer(this._slaves);
    ScheduledExecutorService loadBalSvr = Executors.newScheduledThreadPool(8);
    loadBalSvr.scheduleAtFixedRate(loadbalancer, 0, LOAD_BALANCE_CYCLE_SEC, TimeUnit.SECONDS);
  }

  /**
   * update the info of a slave
   * 
   * @param key
   *          the key of the slave in the map
   * @param wl
   *          the newest workload of this slave
   */
  public void updateSlaveMeta(String key, int wl) {
    if (this._slaves.containsKey(key)) {
      this._slaves.get(key).setAlive(true);
      this._slaves.get(key).setWorkload(wl);
    } else {
      logger.info("No such slave " + key + " in Master now. Register it.");
      
      SlaveMeta newslave = new SlaveMeta(SlaveMeta.getIpFromMapKey(key),
              SlaveMeta.getPortFromMapKey(key), wl, true);
      this._slaves.put(key, newslave);
    }
  }

  /**
   * 
   * @return a copy of the status of all slaves
   */
  public List<SlaveMeta> getSlaves() {
    // create a copy of slave lists to prevent modification
    return new ArrayList<SlaveMeta>(this._slaves.values());
  }

}

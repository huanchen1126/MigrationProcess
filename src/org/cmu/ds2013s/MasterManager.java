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
  
  public static final int ALIVE_CHECK_CYCLE_SEC = 2;
  
  public static final int SCHEDULER_POOL_SIZE = 8;

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
    
    ScheduledExecutorService serviceSche = Executors.newScheduledThreadPool(SCHEDULER_POOL_SIZE);
    
    // start load balancer
    LoadBalancer loadbalancer = new LoadBalancer(this._slaves);
    serviceSche.scheduleAtFixedRate(loadbalancer, LOAD_BALANCE_CYCLE_SEC, LOAD_BALANCE_CYCLE_SEC, TimeUnit.SECONDS);
    
    // start alive checking
    SlaveAliveChecker aliveChecker = new SlaveAliveChecker(this._slaves);
    serviceSche.scheduleAtFixedRate(aliveChecker, ALIVE_CHECK_CYCLE_SEC, ALIVE_CHECK_CYCLE_SEC, TimeUnit.SECONDS);
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

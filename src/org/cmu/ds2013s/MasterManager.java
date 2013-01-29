package org.cmu.ds2013s;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MasterManager implements ManagerContext {
  private static final Log logger = LogFactory.getLog(MasterManager.class);

  private Map<String, SlaveMeta> _slaves; // key is HOST:PORT

  public Console _console;

  public static final int LOAD_BALANCE_CYCLE_SEC = 7;

  public static final int ALIVE_CHECK_CYCLE_SEC = 2;

  public static final int SCHEDULER_POOL_SIZE = 8;

  private int _port;

  private String _ip;

  private SlaveChooserStrategy _scstrategy;

  public MasterManager(int port) {
    this._port = port;
    this._ip = CommunicationUtil.getLocalIPAddress();

    this._slaves = Collections.synchronizedMap(new TreeMap<String, SlaveMeta>());
    this._console = System.console();
    this._scstrategy = new DefaultSlaveChooser();
  }

  @Override
  public void run() {
    logger.info("MasterManager begin running on " + this._ip + ":" + this._port + ".");

    // start network listener
    NetworkListener listener = new NetworkListener(MasterMessageHandler.class, this._port, this);
    (new Thread(listener)).start();

    ScheduledExecutorService serviceSche = Executors.newScheduledThreadPool(SCHEDULER_POOL_SIZE);

    // start load balancer
    LoadBalancer loadbalancer = new LoadBalancer(this);
    serviceSche.scheduleAtFixedRate(loadbalancer, LOAD_BALANCE_CYCLE_SEC, LOAD_BALANCE_CYCLE_SEC,
            TimeUnit.SECONDS);

    // start alive checking
    SlaveAliveChecker aliveChecker = new SlaveAliveChecker(this);
    serviceSche.scheduleAtFixedRate(aliveChecker, ALIVE_CHECK_CYCLE_SEC, ALIVE_CHECK_CYCLE_SEC,
            TimeUnit.SECONDS);

    // start the console
    while (true) {
      String command = this._console.readLine("==> ").trim();
      if (command.equals("")) {
        continue;
      } else if (command.equals("ps")) {
        // TODO: procedure for ps
        continue;
      } else if (command.equals("quit")) {
        System.exit(0);
      } else {
        SlaveMeta slave = null;
        synchronized (this._slaves) {
          slave = this.chooseSlave();
        }

        if (slave != null) {
          NewJobCommand njcmd = new NewJobCommand(this._ip, this._port, command);
          CommunicationUtil.sendCommand(slave.getIp(), slave.getPort(), njcmd.toBytes());
        } else { // no slave exist
          System.err.println("Sorry, no slave connected right now. Your request cannot be done.");
        }
      }
    }
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

  public void deleteSlaveMeta(String key) {
    this._slaves.remove(key);
  }

  /**
   * 
   * @return a copy of the status of all slaves
   */
  public List<SlaveMeta> getSlaves() {
    // create a copy of slave lists to prevent modification
    return new ArrayList<SlaveMeta>(this._slaves.values());
  }

  /**
   * choose a slave to assign new job
   */
  public SlaveMeta chooseSlave() {
    return this._scstrategy.chooseSlave(getSlaves());
  }

}

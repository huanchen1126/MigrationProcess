package org.cmu.ds2013s;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class SlaveManager implements ManagerContext {
  /* processes running on this slave. processes will be removed when terminated */
  HashMap<Thread, MigratableProcess> processes;
  
  /* ip of this slave */
  private String _hostname;

  /* port of this slave */
  private int _port;

  /* ip of master that this slave connect to */
  private String _masterHostname;

  /* port of master that this slave connect to */
  private int _masterPort;
  
  private AtomicInteger currentLoad = new AtomicInteger(0);

  public SlaveManager(int port, String masterHostname, int masterPort) {
    processes = new HashMap<Thread, MigratableProcess>();
    this._hostname = CommunicationUtil.getLocalIPAddress();
    this._port = port;
    this._masterHostname = masterHostname;
    this._masterPort = masterPort;
  }

  @Override
  public void run() {
    Thread heartBeatSender = new Thread(new HeartBeatSender(this));
    heartBeatSender.start();
    Thread listener = new Thread(new NetworkListener(SlaveMessageHandler.class, _port, this));
    listener.start();
    try {
      heartBeatSender.join();
      listener.join();
    } catch (InterruptedException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public String get_hostname() {
    return _hostname;
  }

  public void set_hostname(String _hostname) {
    this._hostname = _hostname;
  }

  public int get_port() {
    return _port;
  }

  public void set_port(int _port) {
    this._port = _port;
  }

  public String get_masterHostname() {
    return _masterHostname;
  }

  public void set_masterHostname(String _masterHostname) {
    this._masterHostname = _masterHostname;
  }

  public int get_masterPort() {
    return _masterPort;
  }

  public void set_masterPort(int _masterPort) {
    this._masterPort = _masterPort;
  }

  public int getCurrentLoad() {
    return currentLoad.get();
  }

  public void setCurrentLoad(int currentLoad) {
    this.currentLoad.set(currentLoad);
  }
  
}

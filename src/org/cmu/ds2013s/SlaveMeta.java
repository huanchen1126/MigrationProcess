package org.cmu.ds2013s;

public class SlaveMeta {

  private String _ip;

  private int _port;

  private int _workload;

  private boolean _isalive;
  
  public SlaveMeta(String ip, int p, int w, boolean alive) {
    this._ip = ip;
    this._port = p;
    this._workload = w;
    this._isalive = alive;
  }

  public String getIp() {
    return _ip;
  }

  public int getPort() {
    return _port;
  }

  public int getWorkload() {
    return _workload;
  }
  
  public void setWorkload(int _workload) {
    this._workload = _workload;
  }

  public boolean isAlive() {
    return _isalive;
  }

  public void setAlive(boolean _isalive) {
    this._isalive = _isalive;
  }

  public static String getMapKey(String host, int port) {
    return host + ":" + port;
  }
  
  public static String getMapKey(SlaveMeta meta) {
    return meta.getIp() + ":" + meta.getPort();
  }
  
  public static String getIpFromMapKey(String key) {
    if (key == null || key.length() == 0) {
      throw new RuntimeException("Invalid Map key.");
    }
    
    String[] ele = key.split(":");
    if (ele.length != 2) {
      throw new RuntimeException("Invalid Map key.");
    }
    
    return ele[0];
      
  }
  
  public static int getPortFromMapKey(String key) {
    if (key == null || key.length() == 0) {
      throw new RuntimeException("Invalid Host:Port.");
    }
    
    String[] ele = key.split(":");
    if (ele.length != 2) {
      throw new RuntimeException("Invalid Host:Port.");
    }
    
    return Integer.parseInt(ele[1]);
  }
}

package org.cmu.ds2013s;

public class SlaveMeta {
  
  private final long ALIVE_CYCLE = 8000; // 8 seconds

  private String _ip;

  private int _port;

  private int _workload;
  
  private long _timestamp;
  
  public SlaveMeta(String ip, int p, int w, long ts) {
    this._ip = ip;
    this._port = p;
    this._workload = w;
    this._timestamp = ts;
    
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

  public boolean isAlive(long curtime) {
    return (curtime - this._timestamp <= ALIVE_CYCLE);
  }
  
  public long getTimeStamp() {
    return this._timestamp;
  }
  
  public void setTimeStamp(long curtime) {
    this._timestamp = curtime;
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

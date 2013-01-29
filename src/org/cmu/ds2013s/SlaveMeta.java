package org.cmu.ds2013s;

import java.util.*;

public class SlaveMeta implements CompositeWorkItem {
  
  private final long ALIVE_CYCLE = 8000; // 8 seconds

  private String _ip;

  private int _port;

  private int _workload;
  
  private long _timestamp;
  
  private Map<String, ProcessMeta> _processes;

  public SlaveMeta(String ip, int p, int w, long ts) {
    this._ip = ip;
    this._port = p;
    this._workload = w;
    this._timestamp = ts;
    
    this._processes = Collections.synchronizedMap(new TreeMap<String, ProcessMeta>());
    
  }
  
  public void addProcess(ProcessMeta pm) {
    this._processes.put(pm.getId(), pm);
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
  
  /**
   * 
   * @param pms
   *          the status string array for this slave
   */
  public void updateProcessMeta(String[] pms) {
    Map<String, ProcessMeta> aliveps = new TreeMap<String, ProcessMeta>();
    
    synchronized (this._processes) {

      for (String pm : pms) {
        String id = pm.substring(0, pm.indexOf(" "));

        if (this._processes.containsKey(id)) {
          aliveps.put(id, this._processes.get(id));
        } else {
          String rawcmd = pm.substring(pm.indexOf(" "));
          aliveps.put(id, new ProcessMeta(rawcmd));
        }
      }
      
      this._processes.clear();
      this._processes.putAll(aliveps);
    }
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

  @Override
  public void showItem() {
    System.out.format("\n Slave %s:%d has %d workloads:\n", this._ip, this._port, this._workload);
    System.out.println("=====================================================================");
    
    for(ProcessMeta pm : this._processes.values())
      pm.showItem();
  }
}

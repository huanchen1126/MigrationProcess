package org.cmu.ds2013s;

import java.util.*;

/**
 * This class is the meta data about each slave nodes
 */
public class SlaveMeta implements CompositeWorkItem {
  
  // the time period of how long at least the slave
  // should send a heartbeat to keep it alive
  private final long ALIVE_CYCLE = 8000; // 8 seconds

  // the ip address of the slave
  private String _ip;

  // the port number of the slave
  private int _port;

  // the workload of this slave
  private int _workload;
  
  // the time at which the slave sends a heartbeat 
  private long _timestamp;
  
  // all the meta data of processes in this slave
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
   * Update the process status according to each heartbeat message
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
          // this process already exists
          aliveps.put(id, this._processes.get(id));
        } else {
          // this process does not exists in master, record it
          String rawcmd = pm.substring(pm.indexOf(" ") + 1);
          aliveps.put(id, new ProcessMeta(id, rawcmd));
        }
      }
      
      // update the processes list
      this._processes.clear();
      this._processes.putAll(aliveps);
    }
  }

  /**
   * Generate the key for a slave in the slavemap
   * 
   * @param meta
   * @return the key in string format
   */
  public static String getMapKey(String host, int port) {
    return host + ":" + port;
  }
  
  /**
   * Generate the key for a slave in the slavemap
   * 
   * @param meta
   * @return the key in string format
   */
  public static String getMapKey(SlaveMeta meta) {
    return meta.getIp() + ":" + meta.getPort();
  }
  
  /**
   * Extract ip address from the key of a slave in the slavemap
   * @param key
   * @return the id address in string format
   */
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
  
  /**
   * Extract port number from the key of a slave in the slavemap
   * @param key
   * @return the port number
   */
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
    System.out.format("\nSlave %s:%d has %d workloads:\n", this._ip, this._port, this._workload);
    System.out.println("=====================================================================");
    
    for(ProcessMeta pm : this._processes.values())
      pm.showItem();
  }
}

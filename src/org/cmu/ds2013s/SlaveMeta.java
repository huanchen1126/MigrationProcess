package org.cmu.ds2013s;

public class SlaveMeta {

  private String _ip;

  private int _port;

  private int _workload;

  private boolean _isalive;

  public String get_ip() {
    return _ip;
  }

  public void set_ip(String _ip) {
    this._ip = _ip;
  }

  public int get_port() {
    return _port;
  }

  public void set_port(int _port) {
    this._port = _port;
  }

  public int get_workload() {
    return _workload;
  }

  public void set_workload(int _workload) {
    this._workload = _workload;
  }

  public boolean is_isalive() {
    return _isalive;
  }

  public void set_isalive(boolean _isalive) {
    this._isalive = _isalive;
  }

}

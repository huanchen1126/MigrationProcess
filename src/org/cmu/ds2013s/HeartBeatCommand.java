package org.cmu.ds2013s;

public class HeartBeatCommand extends Command {
  
  private int _workload;

  protected HeartBeatCommand(CommandType type, byte[] body, int wl, String host, int port) {
    super(type, body, host, port);
    
    this._workload = wl;
  }

  @Override
  public byte[] toBytes() {
    return null;
  }
  
  public int getWorkLoad() {
    return this._workload;
  }

}

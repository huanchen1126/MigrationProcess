package org.cmu.ds2013s;

public class HeartBeatCommand extends Command {
  
  private int _workload;

  protected HeartBeatCommand(CommandType type, String body, int wl, String host, int port) {
    super(type, body, host, port);
    
    this._workload = wl;
  }

  @Override
  public String toText() {
    // TODO : write rule to transcript this command to text for communication
    return null;
  }
  
  public int getWorkLoad() {
    return this._workload;
  }

}

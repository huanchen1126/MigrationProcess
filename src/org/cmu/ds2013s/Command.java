package org.cmu.ds2013s;

public abstract class Command {
  public enum CommandType { HEART_BEAT, MIGRATE_SOURCE, MIGRATE_DEST, MIGRATE_SEND };
  
  private CommandType _type;
  private String _fromHost;
  private int _fromPort;
  
  protected Command(CommandType type, String host, int port) {
    this._fromHost = host;
    this._fromPort = port;
    this._type = type;
  }
  
  public CommandType getType() {
    return this._type;
  }
  
  public String getFromHost(){
    return this._fromHost;
  }
  
  public int getFromPort() {
    return this._fromPort;
  }
  
  public abstract String toText();
  
  public static Command newInstance(String content, String rhost, int rport) {
    
    return null;
  }
}

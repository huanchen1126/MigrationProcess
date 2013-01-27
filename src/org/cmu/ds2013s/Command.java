package org.cmu.ds2013s;

public abstract class Command {
  /**
   * HEART_BEAT : heart beat command, slave -> master
   * MIGRATE_SOURCE : inform a slave to be the source of a process migration, master -> slave
   * MIGRATE_DEST : inform a slave to be the destination of a process migration, master -> slave
   * MIGRATE_SEND : send a serialized process object to the other slave, slave -> slave
   */
  public enum CommandType { HEART_BEAT, MIGRATE_SOURCE, MIGRATE_DEST, MIGRATE_SEND };
  
  private CommandType _type;
  private String _body;
  private String _fromHost;
  private int _fromPort;
  
  protected Command(CommandType type, String body, String host, int port) {
    this._fromHost = host;
    this._fromPort = port;
    this._type = type;
    this._body = body;
  }
  
  public CommandType getType() {
    return this._type;
  }
  
  public String getBody() {
    return this._body;
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

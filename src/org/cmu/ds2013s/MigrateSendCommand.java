package org.cmu.ds2013s;

public class MigrateSendCommand extends Command {

  private byte[] _object;
  
  protected MigrateSendCommand(CommandType type, byte[] body, String host, int port, byte[] objectraw) {
    super(type, body, host, port);
    
    this._object = objectraw;
  }

  @Override
  public byte[] toBytes() {
    return null;
  }

}

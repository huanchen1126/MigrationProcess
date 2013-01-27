package org.cmu.ds2013s;

public class MigrateSendCommand extends Command {

  private byte[] _object;
  
  public MigrateSendCommand(CommandType type, String host, int port, byte[] objectraw) {
    super(type, host, port);
    
    this._object = objectraw;
  }

  @Override
  public byte[] toBytes() {
    return null;
  }

}

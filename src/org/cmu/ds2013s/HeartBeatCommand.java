package org.cmu.ds2013s;

import java.nio.ByteBuffer;

public class HeartBeatCommand extends Command {
  
  private int _workload;

  public HeartBeatCommand(CommandType type, int wl, String host, int port) {
    super(type, host, port);
    
    this._workload = wl;
  }

  @Override
  public byte[] toBytes() {
    int totallen = Command.HEADER_LEN + NUM_LEN;
    byte[] result = new byte[totallen];
    int offset = 0;
    
    // 1. encode command message length
    int msglen = totallen - LENGTH_LEN;
    byte[] msglenbin = ByteBuffer.allocate(LENGTH_LEN).putInt(msglen).array();
    System.arraycopy(msglenbin, 0, result, offset, LENGTH_LEN);
    offset += LENGTH_LEN;
    
    // 2. encode command type
    int cmdtype = this.getType().getValue();
    byte[] typebin = ByteBuffer.allocate(CMD_LEN).putInt(cmdtype).array();
    System.arraycopy(msglenbin, 0, result, offset, CMD_LEN);
    offset += CMD_LEN;
    
    // 3. encode ip
    
    // 4. encode port
    
    // 5. encode workload
    
    return null;
  }
  
  public int getWorkLoad() {
    return this._workload;
  }

}

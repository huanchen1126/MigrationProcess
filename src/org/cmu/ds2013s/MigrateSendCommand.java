package org.cmu.ds2013s;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * this command is sent from one slave to another slave 
 * this command consists of: 
 * 1. the command length 
 * 2. the command type 
 * 3. the ip and port of source slave 
 * 4. the serialized process to be started in the destination slave
 */
public class MigrateSendCommand extends Command {

  private byte[] _object;

  private int _jobid;

  public MigrateSendCommand(String host, int port, int id, byte[] objectraw) {
    super(CommandType.MIGRATE_SEND, host, port);
    this._object = objectraw;
    this._jobid = id;
  }

  /**
   * encode the command to an array of bytes
   */
  @Override
  public byte[] toBytes() {
    int totallen = Command.HEADER_LEN + Command.NUM_LEN + this._object.length;
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
    System.arraycopy(typebin, 0, result, offset, CMD_LEN);
    offset += CMD_LEN;

    // 3. encode ip
    byte[] ipbin = new byte[IP_LEN];
    try {
      ipbin = InetAddress.getByName(this.getHost()).getAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    System.arraycopy(ipbin, 0, result, offset, IP_LEN);
    offset += IP_LEN;

    // 4. encode port
    byte[] portbin = ByteBuffer.allocate(PORT_LEN).putInt(this.getPort()).array();
    System.arraycopy(portbin, 0, result, offset, PORT_LEN);
    offset += PORT_LEN;

    // 5. encode job id
    byte[] jobidbin = ByteBuffer.allocate(NUM_LEN).putInt(this.getJobId()).array();
    System.arraycopy(jobidbin, 0, result, offset, NUM_LEN);
    offset += NUM_LEN;

    // 6. encode serialized object
    System.arraycopy(this._object, 0, result, offset, this._object.length);

    return result;
  }

  public byte[] getObject() {
    return _object;
  }

  public int getJobId() {
    return this._jobid;
  }
}

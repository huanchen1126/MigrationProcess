package org.cmu.ds2013s;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class MigrateSourceCommand extends Command {

  private int _migrateNum;

  public MigrateSourceCommand(String host, int port, int migrateNum) {
    super(CommandType.MIGRATE_SOURCE, host, port);
    _migrateNum = migrateNum;
  }
  
  public MigrateSourceCommand(MigrationTask task) {
    this(task.getTo().getIp(), task.getTo().getPort(), task.getNumber());
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
    byte[] ipbin = new byte[IP_LEN];
    try {
      ipbin = InetAddress.getByName(this.getHost()).getAddress();
      System.arraycopy(ipbin, 0, result, offset, IP_LEN);
      offset += IP_LEN;
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // 4. encode port
    byte[] portbin = ByteBuffer.allocate(PORT_LEN).putInt(this.getPort()).array();
    System.arraycopy(portbin, 0, result, offset, PORT_LEN);
    offset += PORT_LEN;

    // 5. encode migNum
    byte[] migNumbin = ByteBuffer.allocate(NUM_LEN).putInt(_migrateNum).array();
    System.arraycopy(migNumbin, 0, result, offset, NUM_LEN);
    offset += NUM_LEN;

    return result;
  }

  public int get_migrateNum() {
    return _migrateNum;
  }

  public void set_migrateNum(int _migrateNum) {
    this._migrateNum = _migrateNum;
  }
  
}

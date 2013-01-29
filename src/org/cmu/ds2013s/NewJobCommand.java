package org.cmu.ds2013s;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class NewJobCommand extends Command {

  private String _input;

  public NewJobCommand(String host, int port, String in) {
    super(CommandType.NEW_JOB, host, port);
    this._input = in;
  }

  @Override
  public byte[] toBytes() {
    int totallen = Command.HEADER_LEN + this._input.length();
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
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.arraycopy(ipbin, 0, result, offset, IP_LEN);
    offset += IP_LEN;

    // 4. encode port
    byte[] portbin = ByteBuffer.allocate(PORT_LEN).putInt(this.getPort()).array();
    System.arraycopy(portbin, 0, result, offset, PORT_LEN);
    offset += PORT_LEN;

    // 5. encode cmdinput string
    byte[] cmdstrbin = this._input.getBytes();
    System.arraycopy(cmdstrbin, 0, result, offset, cmdstrbin.length);
    offset += cmdstrbin.length;

    return result;
  }

  public String get_input() {
    return _input;
  }

  public void set_input(String _input) {
    this._input = _input;
  }
}

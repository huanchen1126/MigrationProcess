package org.cmu.ds2013s;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * This command to sent from slave to master periodically 
 * It consists: 
 * 1. the command length 
 * 2. the command type 
 * 3. slave ip and port 
 * 4. list of processes running on slave
 * */

public class HeartBeatCommand extends Command {

  private byte[] _processList;

  public HeartBeatCommand(String host, int port, byte[] processList) {
    super(CommandType.HEART_BEAT, host, port);
    this._processList = processList;
  }

  public HeartBeatCommand(String host, int port, String[] processList) {
    super(CommandType.HEART_BEAT, host, port);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutput out = null;
    /* serialize the process list */
    byte[] object = null;
    try {
      out = new ObjectOutputStream(bos);
      out.writeObject(processList);
      object = bos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        out.close();
        bos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    this._processList = object;
  }

  /**
   * encode the command to an array of bytes
   */
  @Override
  public byte[] toBytes() {
    int totallen = Command.HEADER_LEN + this._processList.length;
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

    // 5. encode process list
    System.arraycopy(this._processList, 0, result, offset, this._processList.length);

    return result;
  }

  /**
   * deserialize and return the process list
   * 
   * @return process list in String array
   */
  public String[] get_processList() {
    ByteArrayInputStream in = new ByteArrayInputStream(this._processList);
    String[] processList = null;
    ObjectInputStream is;
    try {
      is = new ObjectInputStream(in);
      processList = (String[]) is.readObject();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return processList;
  }

}

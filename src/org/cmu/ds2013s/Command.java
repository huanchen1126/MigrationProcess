package org.cmu.ds2013s;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public abstract class Command {
  /**
   * HEART_BEAT : heart beat command, slave -> master MIGRATE_SOURCE : inform a slave to be the
   * source of a process migration, master -> slave MIGRATE_DEST : inform a slave to be the
   * destination of a process migration, master -> slave MIGRATE_SEND : send a serialized process
   * object to the other slave, slave -> slave
   */
  public enum CommandType {
    HEART_BEAT(0), MIGRATE_SOURCE(1), MIGRATE_DEST(2), MIGRATE_SEND(3);

    private int _value;

    private CommandType(int v) {
      this._value = v;
    }

    public int getValue() {
      return this._value;
    }
  };

  public static int LENGTH_LEN = Integer.SIZE / Byte.SIZE;

  public static int CMD_LEN = Integer.SIZE / Byte.SIZE;

  public static int IP_LEN = 32 / Byte.SIZE;

  public static int PORT_LEN = Integer.SIZE / Byte.SIZE;

  public static int NUM_LEN = Integer.SIZE / Byte.SIZE;

  private CommandType _type;

  private byte[] _body;

  private String _fromHost;

  private int _fromPort;

  protected Command(CommandType type, byte[] body, String host, int port) {
    this._fromHost = host;
    this._fromPort = port;
    this._type = type;
    this._body = body;
  }

  public CommandType getType() {
    return this._type;
  }

  public byte[] getBody() {
    return this._body;
  }

  public String getFromHost() {
    return this._fromHost;
  }

  public int getFromPort() {
    return this._fromPort;
  }

  public abstract byte[] toBytes();

  public static Command newInstance(byte[] content, String rhost, int rport) {
    Command result = null;

    int offset = 0;

    // 1. get command type value
    int cmdvalue = ByteBuffer.wrap(content, offset, Command.CMD_LEN).getInt();
    Command.CommandType cmdtype = Command.CommandType.values()[cmdvalue];
    offset += Command.CMD_LEN;

    String ipstr = "";
    int port = 0;
    byte[] ipbin = null;
    int migNum = 0;
    // 2. create command according to command type value
    try {
      switch (cmdtype) {
        case HEART_BEAT:
          ipbin = new byte[Command.IP_LEN];
          System.arraycopy(content, offset, ipbin, 0, Command.IP_LEN);
          offset += Command.IP_LEN;

          ipstr = InetAddress.getByAddress(ipbin).getHostAddress();

          port = ByteBuffer.wrap(content, offset, Command.PORT_LEN).getInt();
          offset += Command.PORT_LEN;

          int workload = ByteBuffer.wrap(content, offset, Command.NUM_LEN).getInt();
          offset += Command.NUM_LEN;

          result = new HeartBeatCommand(cmdtype, content, workload, ipstr, port);
          break;
        case MIGRATE_SOURCE:
          ipbin = new byte[Command.IP_LEN];
          System.arraycopy(content, offset, ipbin, 0, Command.IP_LEN);
          offset += Command.IP_LEN;

          ipstr = InetAddress.getByAddress(ipbin).getHostAddress();

          port = ByteBuffer.wrap(content, offset, Command.PORT_LEN).getInt();
          offset += Command.PORT_LEN;

          migNum = ByteBuffer.wrap(content, offset, Command.NUM_LEN).getInt();
          offset += Command.NUM_LEN;

          result = new MigrateSourceCommand(cmdtype, content, ipstr, port, migNum);
        case MIGRATE_DEST:
          ipbin = new byte[Command.IP_LEN];
          System.arraycopy(content, offset, ipbin, 0, Command.IP_LEN);
          offset += Command.IP_LEN;

          ipstr = InetAddress.getByAddress(ipbin).getHostAddress();

          port = ByteBuffer.wrap(content, offset, Command.PORT_LEN).getInt();
          offset += Command.PORT_LEN;

          migNum = ByteBuffer.wrap(content, offset, Command.NUM_LEN).getInt();
          offset += Command.NUM_LEN;

          result = new MigrateDestCommand(cmdtype, content, ipstr, port, migNum);
        case MIGRATE_SEND:
          ipbin = new byte[Command.IP_LEN];
          System.arraycopy(content, offset, ipbin, 0, Command.IP_LEN);
          offset += Command.IP_LEN;

          ipstr = InetAddress.getByAddress(ipbin).getHostAddress();

          port = ByteBuffer.wrap(content, offset, Command.PORT_LEN).getInt();
          offset += Command.PORT_LEN;

          byte[] object = new byte[content.length - offset];
          System.arraycopy(content, offset, object, 0, object.length);

          result = new MigrateSendCommand(cmdtype, content, ipstr, port, object);
      }

    } catch (UnknownHostException e) {
      e.printStackTrace();
    }

    return result;
  }
}

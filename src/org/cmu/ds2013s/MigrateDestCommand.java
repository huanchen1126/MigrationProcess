package org.cmu.ds2013s;

public class MigrateDestCommand extends Command {

  private int _migrateNum;
  
  protected MigrateDestCommand(CommandType type, byte[] body, String host, int port, int migrateNum) {
    super(type, body, host, port);
    _migrateNum = migrateNum;
  }

  @Override
  public byte[] toBytes() {
    return null;
  }

}

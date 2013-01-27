package org.cmu.ds2013s;

public class MigrateDestCommand extends Command {

  private int _migrateNum;
  
  public MigrateDestCommand(CommandType type, String host, int port, int migrateNum) {
    super(type, host, port);
    _migrateNum = migrateNum;
  }

  @Override
  public byte[] toBytes() {
    return null;
  }

}

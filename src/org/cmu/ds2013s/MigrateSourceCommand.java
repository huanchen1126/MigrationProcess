package org.cmu.ds2013s;

public class MigrateSourceCommand extends Command {

  private int _migrateNum;

  public MigrateSourceCommand(CommandType type, String host, int port,
          int migrateNum) {
    super(type, host, port);
    _migrateNum = migrateNum;
  }

  @Override
  public byte[] toBytes() {
    return null;
  }

}

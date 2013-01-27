package org.cmu.ds2013s;

public class MigrateSourceCommand extends Command {

  private int _migrateNum; 
  protected MigrateSourceCommand(CommandType type, String body, String host, int port, int migrateNum) {
    super(type, body, host, port);
    _migrateNum = migrateNum;
  }

  @Override
  public String toText() {
    return String.valueOf(_migrateNum);
  }

}

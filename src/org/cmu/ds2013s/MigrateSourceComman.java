package org.cmu.ds2013s;

public class MigrateSourceComman extends Command {

  private int _migrateNum; 
  protected MigrateSourceComman(CommandType type, String host, int port, int migrateNum) {
    super(type, host, port);
    _migrateNum = migrateNum;
  }

  @Override
  public String toText() {
    return String.valueOf(_migrateNum);
  }

}

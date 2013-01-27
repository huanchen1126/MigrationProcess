package org.cmu.ds2013s;

public class MigrationTask {
  private SlaveMeta _from;
  private SlaveMeta _to;
  private int _number;
  
  public MigrationTask(SlaveMeta f, SlaveMeta t, int num) {
    this._from = f;
    this._to = t;
    this._number = num;
  }
  
  public SlaveMeta getFrom() {
    return _from;
  }

  public SlaveMeta getTo() {
    return _to;
  }

  public int getNumber() {
    return _number;
  }
}

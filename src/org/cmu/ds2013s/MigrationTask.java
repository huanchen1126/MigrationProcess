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
  
  public SlaveMeta get_from() {
    return _from;
  }

  public SlaveMeta get_to() {
    return _to;
  }

  public int get_number() {
    return _number;
  }
}

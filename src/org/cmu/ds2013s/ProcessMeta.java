package org.cmu.ds2013s;

public class ProcessMeta implements CompositeWorkItem {
  private String _rawcmd;
  
  private String _className;
  
  private String _id;
  
  public ProcessMeta(String id, String rawcmd) {
    this._id = id;
    this._rawcmd = rawcmd;
    this._className = "";

    if (rawcmd.indexOf(" ") > 0) // the cmd has arguments
      this._className = rawcmd.substring(0, rawcmd.indexOf(" "));
    else
      this._className = rawcmd;
  }
  
  public String getId() {
    return this._id;
  }
  
  public String getClassName() {
    return this._className;
  }
  
  public String getRawCommand() {
    return this._rawcmd;
  }

  @Override
  public void showItem() {
    System.out.format("%s\t%s\n", this._id, this._className);
  }
}

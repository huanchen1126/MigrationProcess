package org.cmu.ds2013s;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TestMigrateProcess implements MigratableProcess{
  private static final Log logger = LogFactory.getLog(TestMigrateProcess.class);
  volatile boolean suspend = false;
  String[] args = null;
  public TestMigrateProcess(String[] args){
    this.args = args;
  }
  @Override
  public void run() {
    // TODO Auto-generated method stub
    while(!suspend){
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    logger.info("DOING TO TERMINATE..................");
    suspend = false;
  }

  @Override
  public void suspend() {
    // TODO Auto-generated method stub
    suspend=true;
    while(suspend);
  }
  
  public String toString(){
    String cmd = this.getClass().getName();
    for(String s : args){
      cmd = cmd + " " + s;
    }
    return cmd;
  }

}

package org.cmu.ds2013s;

public class TestMigrateProcess implements MigratableProcess{
  boolean suspend = false;
  public TestMigrateProcess(String[] args){
    
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
    suspend = false;
  }

  @Override
  public void suspend() {
    // TODO Auto-generated method stub
    suspend=true;
    while(suspend);
  }

}

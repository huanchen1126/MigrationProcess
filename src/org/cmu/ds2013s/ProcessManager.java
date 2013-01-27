package org.cmu.ds2013s;

public class ProcessManager {
  
  private ProcessManager() {
    
  }

  public static void main(String[] args) {
    Runnable mainProcedure = createManager(args);
    
    if (mainProcedure == null)
      return ;
    else {
      Thread main = new Thread(mainProcedure);
      main.run();
      
      try {
        main.join();
      } catch (InterruptedException e) {
        System.err.println("Main procedure interupted unexpectedly.");
      }
    }
    
    
  }

  /**
   * Factory method go generate the main procedure of current process,
   * whether Master or Slave.
   * 
   * @param system arguments
   * @return return a runnable as the main procedure of current process,
   *        if arguments are invalid, return null.
   */
  public static Runnable createManager(String[] arg) {
    if (arg == null || arg.length == 0) {
      System.err.println("Usage: ProcessManager <port> [-c <hostname>]");
      return null;
    }
    
    try {
      int port = Integer.parseInt(arg[0]);
      
      if (arg.length == 3 && arg[1].compareTo("-c") == 0) {
        String hostname = arg[2];
        
        return new SlaveManager();
      }else {
        return new MasterManager(port);
      }
    }catch (NumberFormatException e) {
      System.err.println("Invalid port number.");
      return null;
    }
  }
  
}

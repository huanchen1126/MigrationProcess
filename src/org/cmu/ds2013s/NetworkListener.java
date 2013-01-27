package org.cmu.ds2013s;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NetworkListener implements Runnable {
  private static final Log logger = LogFactory.getLog(NetworkListener.class);
  private Class _class;
  private ManagerContext _context; 
  private int _port = 3333;

  public NetworkListener(Class c, int port, ManagerContext context) {
    try {
      _class = c;
      _port = port;
      if(!_class.isAssignableFrom(MessageHandler.class)){
        throw new Exception("Class "+c.getName()+" is not subclass of MessageHandler");
      }
      this._context = context;
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    ServerSocket serverSocket = null;
    boolean listening = true;
    try {
      serverSocket = new ServerSocket(_port);
      while (listening) {
        logger.info("Listening");
        // Thread thread = new Thread(new handler(serverSocket.accept()));
        try {
          Thread thread = new Thread((Runnable) _class.getConstructor(Socket.class, ManagerContext.class).newInstance(
                  serverSocket.accept()));
          thread.start();
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (SecurityException e) {
          e.printStackTrace();
        } catch (InstantiationException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        }
        logger.info("Accepted");
      }
      serverSocket.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}

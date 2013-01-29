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
      logger.info("Received a command from master");
      _class = c;
      _port = port;
      if (!MessageHandler.class.isAssignableFrom(_class)) {
        throw new Exception("Class " + c.getName() + " is not subclass of MessageHandler");
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
      serverSocket = new ServerSocket(this._port);
      while (listening) {
        logger.info("Listening");
        // Thread thread = new Thread(new handler(serverSocket.accept()));
        Socket socket = serverSocket.accept();
        //Object[] objargs1 = {socket};
        //Object[] objargs2 = {_context};
        Object[] objargs = {socket,_context};
        Thread thread = new Thread((Runnable) _class.getConstructor(Socket.class,
                ManagerContext.class).newInstance(objargs));
        thread.start();
        logger.info("Accepted");
      }
    } catch (IOException e) {
      e.printStackTrace();
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
    }finally{
      try {
        serverSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

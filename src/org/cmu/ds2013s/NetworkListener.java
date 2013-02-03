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
      if (ProcessManager.DEBUG) {
        logger.info("Received a command from master");
      }
      _class = c;
      _port = port;
      if (!MessageHandler.class.isAssignableFrom(_class)) {
        throw new Exception("Class " + c.getName() + " is not subclass of MessageHandler");
      }
      this._context = context;
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (Exception e) {
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
        if (ProcessManager.DEBUG) {
          logger.info("Listening");
        }
        Socket socket = serverSocket.accept();
        Object[] objargs = {socket,_context};
        Thread thread = new Thread((Runnable) _class.getConstructor(Socket.class,
                ManagerContext.class).newInstance(objargs));
        thread.start();
        if (ProcessManager.DEBUG) {
          logger.info("Accepted");
        }
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

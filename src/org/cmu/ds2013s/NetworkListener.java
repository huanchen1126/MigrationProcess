package org.cmu.ds2013s;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkListener implements Runnable {

  Class theClass;

  public NetworkListener(MessageHandler handler) {
    theClass = handler.getClass();
  }

  @Override
  public void run() {
    ServerSocket serverSocket = null;
    boolean listening = true;
    try {
      serverSocket = new ServerSocket(3333);
      while (listening) {
        System.out.println("Listening");
        // Thread thread = new Thread(new handler(serverSocket.accept()));
        try {
          Thread thread = new Thread((Runnable) theClass.getConstructor(Socket.class).newInstance(
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
        System.out.println("Accepted");
      }
      serverSocket.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}

package org.cmu.ds2013s;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommunicationUtil {

  /**
   *  
   * @return The IP address of local machine in String format.
   */
  public static String getLocalIPAddress() {
    InetAddress addr = null;;
    
    try {
      addr = InetAddress.getLocalHost();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    
    if (addr == null)
      return null;
    else
      return addr.getHostAddress();
  }
  /**
   * send command
   * */
  public static void sendCommand(String ip, int port, byte[] content){
    Socket socket = null;
    try {
      socket = new Socket(ip,port);
      OutputStream out = socket.getOutputStream(); 
      out.write(content);
      out.close();
      socket.close();
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}

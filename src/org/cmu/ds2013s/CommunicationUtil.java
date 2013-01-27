package org.cmu.ds2013s;

import java.net.InetAddress;
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
}

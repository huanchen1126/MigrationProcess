package org.cmu.ds2013s;

import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class MessageHandler implements Runnable{
  
  private static final Log logger = LogFactory.getLog(MessageHandler.class);
  
  private Socket _socket;
  
  private Command _cmd;
  
  public MessageHandler(Socket s) {
    this._socket = s;
    this._cmd = this.parseCommand();
  }
  
  protected Command getCommand() {
    return this._cmd;
  }
  
  protected Socket getSocket() {
    return this._socket;
  }
  
  /**
   * read command text from socket, parse and wrap it
   * into a Command
   * 
   * @return return a specific command; if error occurs
   * during reading socket, return null. 
   */
  private Command parseCommand() {
    if (this._socket == null) {
      logger.error("Socket is invalid.");
      throw new RuntimeException("Socket is invalid. Cannot read a command.");
    }
    
    String remotehost = this._socket.getRemoteSocketAddress().toString();
    int remoteport = this._socket.getPort();
    
    // get the content of the command
    String content = null;
    // TODO: read the command text from the socket
    
    return Command.newInstance(content, remotehost, remoteport);
  }
  
  /**
   * sub classes has to implement this method to use
   * deal with current command
   */
  public abstract void handle();

  /**
   * the thread just do command handling, so the run()
   * method should just call handle(), and cannot be
   * overrided.
   */
  public final void run() {
    this.handle();
  }
  
}

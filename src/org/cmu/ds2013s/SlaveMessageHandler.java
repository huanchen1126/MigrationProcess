package org.cmu.ds2013s;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SlaveMessageHandler extends MessageHandler {
  private static final Log logger = LogFactory.getLog(SlaveMessageHandler.class);

  private MasterManager _context;

  public SlaveMessageHandler(Socket s, ManagerContext context) {
    super(s);
    this._context = (MasterManager) context;
  }

  @Override
  public void handle() {
    Command command = this.getCommand();
    switch (command.getType()) {
      case MIGRATE_SOURCE:
        logger.info("Slave received command MIGRATE_SOURCE");
        break;
      case MIGRATE_DEST:
        logger.info("Slave received command MIGRATE_DEST");
        break;
      case MIGRATE_SEND:
        logger.info("Slave received command MIGRATE_SEND");
        break;
      default:
        break;
    }
  }

  public void handleSource(Command command) {
    
  }

  public void handleDest(Command command) {

  }

  public void handleSend(Command command) {

  }

}

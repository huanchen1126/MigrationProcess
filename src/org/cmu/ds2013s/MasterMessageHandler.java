package org.cmu.ds2013s;

import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MasterMessageHandler extends MessageHandler {

  private static final Log logger = LogFactory.getLog(MasterMessageHandler.class);

  private MasterManager _context;
  
  public MasterMessageHandler(Socket s, ManagerContext context) {
    super(s);
    this._context = (MasterManager) context;
  }

  @Override
  public void handle() {
    Command cmd = this.getCommand();

    switch (cmd.getType()) {
      case HEART_BEAT:
        // TODO: handle heart beat command
        String slavekey = SlaveMeta.getMapKey(cmd.getFromHost(), cmd.getFromPort());
        
        break;
      default:
        break;
    }
  }

}

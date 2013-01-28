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
    
    logger.info("Create a new Master Message Handler.");
  }

  @Override
  public void handle() {
    Command cmd = this.getCommand();

    switch (cmd.getType()) {
      case HEART_BEAT:
        handleHeartBeat(cmd);
        break;
      default:
        logger.debug("Invalid command for master.");
        break;
    }
  }
  
  public void handleHeartBeat(Command cmd) {
    HeartBeatCommand hbcmd = (HeartBeatCommand) cmd;
    String slavekey = SlaveMeta.getMapKey(hbcmd.getHost(), hbcmd.getPort());
    
    logger.info("Received a HEARTBEAT from " + slavekey + ". Load is " + hbcmd.getWorkLoad());
    
    this._context.updateSlaveMeta(slavekey, hbcmd.getWorkLoad());    
  }

}

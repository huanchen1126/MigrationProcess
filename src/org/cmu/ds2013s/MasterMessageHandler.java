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
        handleHeartBeat(cmd);
        break;
      default:
        logger.debug("Invalid command for master. Content : " + cmd.getBody());
        break;
    }
  }
  
  public void handleHeartBeat(Command cmd) {
    HeartBeatCommand hbcmd = (HeartBeatCommand) cmd;
    String slavekey = SlaveMeta.getMapKey(hbcmd.getFromHost(), hbcmd.getFromPort());
    
    logger.debug("Received a HEARTBEAT from " + slavekey + ". Content: " + hbcmd.getBody());
    
    this._context.updateSlaveMeta(slavekey, hbcmd.getWorkLoad());    
  }

}

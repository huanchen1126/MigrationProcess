package org.cmu.ds2013s;

import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The handler is used for master node to make responds to incoming messages.
 * Basically, in this system, the master node would only receive HEART_BEAT messages.
 */
public class MasterMessageHandler extends MessageHandler {

  private static final Log logger = LogFactory.getLog(MasterMessageHandler.class);

  // the master manager reference to the master which use this handler 
  private MasterManager _context;
  
  public MasterMessageHandler(Socket s, ManagerContext context) {
    super(s);
    this._context = (MasterManager) context;
    
    if (ProcessManager.DEBUG) {
      logger.info("Create a new Master Message Handler.");
    }
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
  
  private void handleHeartBeat(Command cmd) {
    HeartBeatCommand hbcmd = (HeartBeatCommand) cmd;
    String slavekey = SlaveMeta.getMapKey(hbcmd.getHost(), hbcmd.getPort());
    
    if (ProcessManager.DEBUG) {
      logger.info("Received a HEARTBEAT from " + slavekey + ". Load is "
              + hbcmd.get_processList().length);
    }
    
    this._context.updateSlaveMeta(slavekey, hbcmd.get_processList().length, hbcmd.get_processList());    
  }

}

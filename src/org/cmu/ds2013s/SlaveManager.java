package org.cmu.ds2013s;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class SlaveManager implements ManagerContext {
  //public Vector<Thread> processes;
  HashMap<Thread, MigratableProcess> processes;
  public Console console;

  private String _hostname;

  private int _port;

  private String _masterHostname;

  private int _masterPort;
  
  private AtomicInteger currentLoad = new AtomicInteger(0);

  public SlaveManager(int port, String masterHostname, int masterPort) {
    //processes = new Vector<Thread>();
    processes = new HashMap<Thread, MigratableProcess>();
    console = System.console();
    this._hostname = CommunicationUtil.getLocalIPAddress();
    this._port = port;
    this._masterHostname = masterHostname;
    this._masterPort = masterPort;
  }

  @Override
  public void run() {
    Thread heartBeatSender = new Thread(new HeartBeatSender(this));
    heartBeatSender.start();
    Thread listener = new Thread(new NetworkListener(SlaveMessageHandler.class, _port, this));
    listener.start();
    while (true) {
      String command = console.readLine("==> ").trim();
      if (command.equals("")) {
        continue;
      } else if (command.equals("ps")) {
        for (Thread process : processes.keySet()) {
          System.out.println(process.getName());
        }
      } else if (command.equals("quit")) {
        System.exit(0);
      } else {
        int spaceIndex = command.indexOf(' ');
        String className = "";
        String[] arguments = null;

        if (spaceIndex == -1) {
          className = command;
        } else {
          className = command.substring(0, spaceIndex);
          arguments = command.substring(spaceIndex + 1).split(" ");
        }

        try {
          Class theClass = Class.forName(className);
          MigratableProcess process = (MigratableProcess) theClass.getConstructor(String[].class)
                  .newInstance(arguments);
          Thread thread = new Thread(process);
          thread.setName(command);
          processes.put(thread, process);
        } catch (ClassNotFoundException e) {
          console.printf("Cannot find that class.");
        } catch (IllegalArgumentException e) {
          console.printf("Illegal argument for this class.");
        } catch (SecurityException e) {
          console.printf("Illegal argument for this class.");
        } catch (InstantiationException e) {
          console.printf("Illegal argument for this class.");
        } catch (IllegalAccessException e) {
          console.printf("Illegal argument for this class.");
        } catch (InvocationTargetException e) {
          console.printf("Illegal argument for this class.");
        } catch (NoSuchMethodException e) {
          console.printf("Illegal argument for this class.");
        }
      }
    }
  }

  public String get_hostname() {
    return _hostname;
  }

  public void set_hostname(String _hostname) {
    this._hostname = _hostname;
  }

  public int get_port() {
    return _port;
  }

  public void set_port(int _port) {
    this._port = _port;
  }

  public String get_masterHostname() {
    return _masterHostname;
  }

  public void set_masterHostname(String _masterHostname) {
    this._masterHostname = _masterHostname;
  }

  public int get_masterPort() {
    return _masterPort;
  }

  public void set_masterPort(int _masterPort) {
    this._masterPort = _masterPort;
  }

  public int getCurrentLoad() {
    return currentLoad.get();
  }

  public void setCurrentLoad(int currentLoad) {
    this.currentLoad.set(currentLoad);
  }
  
}

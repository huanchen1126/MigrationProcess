package org.cmu.ds2013s;

import java.io.Console;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public class SlaveManager implements Runnable {
  LinkedList<Thread> processes = new LinkedList<Thread>();

  Console console = System.console();

  @Override
  public void run() {
    while (true) {
      String command = console.readLine("==> ").trim();
      if (command.equals("")) {
        continue;
      } else if (command.equals("ps")) {
        for (Thread process : processes) {
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
          processes.add(thread);
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

}

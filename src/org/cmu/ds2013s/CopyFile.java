package org.cmu.ds2013s;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CopyFile implements MigratableProcess {

  private static final Log logger = LogFactory.getLog(TestMigrateProcess.class);

  volatile boolean suspend = false;

  String[] args = null;

  TransactionFileInputStream inputStream = null;

  TransactionFileOutputStream outputStream = null;

  public CopyFile(String[] args) {
    if (args.length != 2)
      return;
    this.args = args;
    String input = args[0];
    String output = args[1];

    this.inputStream = new TransactionFileInputStream(input);
    this.outputStream = new TransactionFileOutputStream(output);
  }

  @Override
  public void run() {
    PrintWriter writer = new PrintWriter(this.outputStream);
    while (!suspend) {
      try {
        String s = this.inputStream.readLine();
        if (s == null)
          break;
        writer.println(s);
        writer.flush();
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    if (writer != null)
      writer.close();
    logger.info("DOING TO TERMINATE..................");
    suspend = false;
  }

  @Override
  public void suspend() {
    // TODO Auto-generated method stub
    suspend = true;
    while (suspend)
      ;
  }

  public String toString() {
    String cmd = this.getClass().getName();
    if (args == null)
      return cmd;
    for (String s : args) {
      cmd = cmd + " " + s;
    }
    return cmd;
  }

}

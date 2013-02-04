package org.cmu.ds2013s;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grep implements MigratableProcess {

  private volatile boolean suspend = false;

  private int lineNumber;

  private TransactionFileInputStream inputStream;

  private TransactionFileOutputStream outputStream;

  private String[] args;
  
  private String regex;
  
  private Pattern linePattern;

  public Grep(String[] args) {
    this.args = args;

    this.lineNumber = 1;

    if (args.length != 3)
      throw new RuntimeException("Not enough arguments.");

    this.regex = args[0];
    String infile = args[1];
    String outfile = args[2];

    this.linePattern = Pattern.compile(this.regex);
    this.inputStream = new TransactionFileInputStream(infile);
    this.outputStream = new TransactionFileOutputStream(outfile);
  }

  @Override
  public void run() {
    PrintWriter writer = new PrintWriter(this.outputStream);
    Matcher pm = null;
    try {
      while (!suspend) {
        String line;

        line = this.inputStream.readLine();

        if (line == null)
          break;
        
        if (pm == null) {
          pm = this.linePattern.matcher(line);
        }else{
          pm.reset(line);
        }
        
        if (pm.find()) {
          writer.write(this.lineNumber + ":\t" + line);
          writer.flush();
          Thread.sleep(100);
        }
        
        this.lineNumber ++;
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      if (writer != null)
        writer.close();
    }
   
    suspend = false;
  }

  @Override
  public void suspend() {
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

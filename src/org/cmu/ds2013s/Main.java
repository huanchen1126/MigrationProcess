package org.cmu.ds2013s;

import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    CommunicationUtil.sendCommand("128.237.125.238", 12346, "hello".getBytes());
  }
  
  public static void SampleTransactionFileStream(String[] args) {
    Scanner scanner = new Scanner(new TransactionFileInputStream(args[0]));
    PrintWriter writer = new PrintWriter(new TransactionFileOutputStream(args[1]));
    
    while (scanner.hasNext()) {
      writer.println(scanner.nextLine());
      writer.flush();
    }
    
    writer.close();
  }
}
package org.cmu.ds2013s;

import java.io.*;

public class TransactionFileOutputStream extends OutputStream implements Serializable {

  private String _fileName;

  private long _offset; // offset is not necessary for output stream

  private boolean _firstWrite;
  
  public TransactionFileOutputStream(String fn) {
    this(fn, false);
  }

  public TransactionFileOutputStream(String fn, boolean isappend) {
    this._fileName = fn;
    this._offset = 0;
    this._firstWrite = !isappend;
  }

  @Override
  public synchronized void write(byte[] b, int off, int len) throws IOException {
    // open the stream with append mode
    BufferedOutputStream outstream = new BufferedOutputStream(new FileOutputStream(this._fileName,
            !this._firstWrite));
    this._firstWrite = false;

    outstream.write(b, off, len);
    outstream.flush();

    // close the stream
    outstream.close();
  }

  @Override
  public synchronized void write(byte[] b) throws IOException {
    // open the stream with append mode
    BufferedOutputStream outstream = new BufferedOutputStream(new FileOutputStream(this._fileName,
            !this._firstWrite));
    this._firstWrite = false;

    outstream.write(b);
    outstream.flush();

    // close the stream
    outstream.close();
  }

  @Override
  public synchronized void write(int b) throws IOException {
    // open the stream with append mode
    BufferedOutputStream outstream = new BufferedOutputStream(new FileOutputStream(this._fileName,
            !this._firstWrite));
    this._firstWrite = false;

    outstream.write(b);
    outstream.flush();

    // close the stream
    outstream.close();
  }

}

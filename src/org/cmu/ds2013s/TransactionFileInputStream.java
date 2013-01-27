package org.cmu.ds2013s;

import java.io.*;

public class TransactionFileInputStream extends InputStream implements Serializable {

  private String _fileName;
  
  private long _offset;
  
  public TransactionFileInputStream(String fn) {
    this._fileName = fn;
    this._offset = 0;
  }
  
  @Override
  public synchronized void reset() throws IOException {
    super.reset();
    
    this._offset = 0;
  }

  @Override
  public synchronized int read() throws IOException {
    // open the stream
    BufferedInputStream instream = new BufferedInputStream(new FileInputStream(this._fileName));
    
    // skip to the right place
    long actoff = instream.skip(this._offset);
    if (actoff != this._offset)
      throw new RuntimeException("Seek offset error");
    
    int result = instream.read();
    this._offset ++;
    
    // close the stream
    instream.close();
    
    return result;
  }

  @Override
  public synchronized int read(byte[] buffer, int off, int len) throws IOException {
    // open the stream
    BufferedInputStream instream = new BufferedInputStream(new FileInputStream(this._fileName));
    
    // skip to the right place
    long actoff = instream.skip(this._offset);
    if (actoff != this._offset)
      throw new RuntimeException("Seek offset error");
    
    int result = instream.read(buffer, off, len);
    if (result != -1) {
      this._offset += result;
    }
    
    // close the stream;
    instream.close();
    
    return result;
  }

  @Override
  public synchronized int read(byte[] buffer) throws IOException {
    // open the stream
    BufferedInputStream instream = new BufferedInputStream(new FileInputStream(this._fileName));
    
    // skip to the right place
    long actoff = instream.skip(this._offset);
    if (actoff != this._offset)
      throw new RuntimeException("Seek offset error");
    
    int result = instream.read(buffer);
    if (result != -1) {
      this._offset += result;
    }
    
    // close the stream;
    instream.close();
    
    return result;
  }
  
  

}

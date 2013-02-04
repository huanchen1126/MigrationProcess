package org.cmu.ds2013s;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebpageCrawler implements MigratableProcess {
  private static final Log logger = LogFactory.getLog(WebpageCrawler.class);

  /* only crawl NUM_PAGE pages */
  private static final int NUM_PAGE = 1000;
  private int count = 0;
  volatile boolean suspend = false;
  /* queue for urls */
  private Queue<String> queue;
  /* for removing duplicates */
  private HashSet<String> hs;
  
  /* regex to extract ref links */
  private static String _PatternStr = "(?mis)href=\"(.*?)\"";
  private Pattern _Pattern;
  
  /* root of target website */
  private String root;

  /* args, stored for toString() */
  private String[] args = null;

  /* output */
  TransactionFileOutputStream outputStream = null;

  /* two arguments, first one is the url, second one is the output file */
  public WebpageCrawler(String[] args) {
    if (args.length != 2)
      return;
    queue = new LinkedList<String>();
    hs = new HashSet<String>();
    this._Pattern = Pattern.compile(_PatternStr);
    String url = args[0];
    
    /** get root of the url, 
     * e.g the root for www.cmu.edu is cmu.edu, 
     * the root for cmu.edu/global is cmu.edu/global*/
    root = url.substring(url.indexOf('.')+1);
    if(root.indexOf('.')==-1)
      root = url;
    
    root = root.replace("http://", "");
    root = root.replace("https://", "");
    /* End get root */
    
    if(url.indexOf("http")==-1)
      url = "http://" + url;
    queue.add(url);
    hs.add(url);
    
    this.args = args;   
    
    String output = args[1];
    this.outputStream = new TransactionFileOutputStream(output);
  }

  @Override
  public void run() {
    PrintWriter writer = new PrintWriter(this.outputStream);
    while (!suspend) {
      String url = queue.poll();
      if (url == null)
        break;
      String html = getHTML(url);
      writer.println(html);
      writer.flush();
      
      /* if exceeds num of pages needed, stop */
      if(count++>this.NUM_PAGE)
        break;
      
      Matcher patternM = _Pattern.matcher(html);
      while (patternM.find()) {
        String newUrl = patternM.group(1);
        /* if this new url belongs to root, start with "http", and have not downloaded yet, add it to queue */
        if(newUrl.indexOf(root)!=-1 && newUrl.startsWith("http") && !hs.contains(newUrl)){
          queue.add(newUrl);
        }        
      }
    }
    if (writer != null)
      writer.close();
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

  public String getHTML(String targetURL) {
    URL url;
    HttpURLConnection conn;
    BufferedReader rd;
    String line;
    StringBuilder result = new StringBuilder();
    try {
      url = new URL(targetURL);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      while ((line = rd.readLine()) != null) {
        result.append(line);
      }
      rd.close();
    }catch (Exception e) {
      return "";
    }  
    return result.toString();
  }
}

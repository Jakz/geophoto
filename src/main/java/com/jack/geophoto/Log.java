package com.jack.geophoto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log
{
  private static enum Level
  {
    error,
    warning,
    debug,
    verbose
  }
  
  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
  
  private static class Entry
  {
    public final Level level;
    public final String target;
    public final String text;
    public final LocalDateTime timestamp;
    
    Entry(Level level, String target, String text, Object... args)
    {
      this.level = level;
      this.target = target;
      this.text = String.format(text, args);
      this.timestamp = LocalDateTime.now();
    }
    
    public String toString()
    {
      return String.format("%s [%s] [%s] %s", formatter.format(timestamp), level, target, text);
    }
  }
  
  public static void d(String target, String string, Object... args)
  {
    Entry entry = new Entry(Level.debug, target, string, args);
    System.out.println(entry.toString());
  }
}

package com.github.jakz.geophoto.tools;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;

import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.NonConvertedTag;
import com.thebuzzmedia.exiftool.core.StandardTag;
import java.io.IOException;

public class Exif<T extends Exifable>
{
  private final ExifTool exifTool;
  private final ThreadPoolExecutor pool;
  private final AtomicLong counter;
  
  public Exif(int poolSize)
  {
    pool = new CheckedThreadPoolExecutor(poolSize);
   // enableDebug();
    exifTool = new ExifToolBuilder()
        .withPath(Paths.get("./tools/exiftool").toFile())
        .enableStayOpen(5000)
        .withPoolSize(poolSize)
        .build();
    
    counter = new AtomicLong(0L);
  }
  
  public void enableDebug()
  {
    System.setProperty("exiftool.debug", "true");
  }
  
  public void dispose() throws Exception
  {
    pool.shutdownNow();
    exifTool.close();
  }
  
  public void close() throws Exception
  {
    pool.shutdown();
    exifTool.close();
  }
  
  public void waitUntilFinished()
  {
    while (counter.get() != 0);
  }

  protected <U> Future<U> asyncFetch(Callable<U> task)
  {
    counter.incrementAndGet();
    return pool.submit(() -> {
      U t = task.call();
      counter.decrementAndGet();
      return t;
    });
  }
  
  public void asyncFetch(T photo, BiConsumer<T, ExifResult> callback, Tag... tags)
  {
    ExifFetchTask<T> task = new ExifFetchTask<>(photo, this, tags);
    ExifConsumeTask<T> ctask = new ExifConsumeTask<>(photo, task, callback);
    counter.incrementAndGet();
    pool.submit(() -> { ctask.run(); counter.decrementAndGet(); });
  }
  
  public void asyncFetch(T photo, BiConsumer<T, ExifResult> process, BiConsumer<T, ExifResult> after, Tag... tags)
  {
    asyncFetch(photo, process.andThen(after), tags);
  }

  
  ExifTool getTool() { return exifTool; }
}

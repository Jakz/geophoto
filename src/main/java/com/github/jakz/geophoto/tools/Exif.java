package com.github.jakz.geophoto.tools;

import java.nio.file.Paths;
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
import com.thebuzzmedia.exiftool.core.StandardTag;
import java.io.IOException;

public class Exif<T extends Exifable>
{
  private final ExifTool exifTool;
  private final ThreadPoolExecutor pool;
  private final AtomicLong counter;
  
  public Exif(int poolSize)
  {
    pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize);
    exifTool = new ExifToolBuilder().withPath(Paths.get("./tools/exiftool").toFile()).enableStayOpen(5000).withPoolSize(poolSize).build();
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

  protected <T> Future<T> asyncFetch(Callable<T> task)
  {
    counter.incrementAndGet();
    return pool.submit(() -> {
      T t = task.call();
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
  
  public static Coordinate parseGpxTags(ExifResult v)
  {
    if (!v.has(StandardTag.GPS_LATITUDE) || !v.has(StandardTag.GPS_LONGITUDE))
      return new Coordinate(Double.NaN, Double.NaN);
    else
    {
      if (v.has(StandardTag.GPS_ALTITUDE))
        return new Coordinate(
            v.get(StandardTag.GPS_LATITUDE),
            v.get(StandardTag.GPS_LONGITUDE),
            v.get(StandardTag.GPS_ALTITUDE)
        );
      else
        return new Coordinate(
            v.get(StandardTag.GPS_LATITUDE),
            v.get(StandardTag.GPS_LONGITUDE)
        );             
    }
  }
  public Coordinate loadCoordinate(T photo) throws IOException
  {
    ExifFetchTask<T> task = new ExifFetchTask<>(photo, this, StandardTag.GPS_LATITUDE, StandardTag.GPS_LONGITUDE, StandardTag.GPS_ALTITUDE);
    DerivedTask<ExifResult, Coordinate> dtask = new DerivedTask<>(task, v -> parseGpxTags(v));
    Future<Coordinate> coord = asyncFetch(dtask);
    
    try
    {
      return coord.get();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return null;
    }
  }
  
  ExifTool getTool() { return exifTool; }
}

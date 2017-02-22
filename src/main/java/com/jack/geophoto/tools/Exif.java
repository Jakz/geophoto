package com.jack.geophoto.tools;

import java.nio.file.Paths;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiConsumer;

import com.jack.geophoto.data.Coordinate;
import com.jack.geophoto.data.Photo;
import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;
import java.io.IOException;

public class Exif
{
  private final ExifTool exifTool;
  private final ThreadPoolExecutor pool;
  
  public Exif(int poolSize)
  {
    pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(poolSize);
    exifTool = new ExifToolBuilder().withPath(Paths.get("./tools/exiftool").toFile()).enableStayOpen(5000).withPoolSize(poolSize).build();
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
  
  protected <T> Future<T> asyncFetch(Callable<T> task)
  {
    return pool.submit(task);
  }
  
  public void asyncFetch(Photo photo, BiConsumer<Photo, ExifResult> callback, Tag... tags)
  {
    ExifFetchTask task = new ExifFetchTask(photo, this, tags);
    ExifConsumeTask ctask = new ExifConsumeTask(photo, task, callback);
    pool.submit(ctask);
  }
  
  public void asyncFetch(Photo photo, BiConsumer<Photo, ExifResult> process, BiConsumer<Photo, ExifResult> after, Tag... tags)
  {
    asyncFetch(photo, process.andThen(after), tags);
  }
 
  public Coordinate loadCoordinate(Photo photo) throws IOException
  {
    ExifFetchTask task = new ExifFetchTask(photo, this, StandardTag.GPS_LATITUDE, StandardTag.GPS_LONGITUDE, StandardTag.GPS_ALTITUDE);
    DerivedTask<ExifResult, Coordinate> dtask = new DerivedTask<>(task, v -> Coordinate.parse(v));
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

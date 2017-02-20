package com.jack.geophoto.tools;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.jack.geophoto.data.Coordinate;
import com.jack.geophoto.data.Photo;
import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;
import com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException;

import static java.util.Arrays.asList;

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
  
  protected <T> Future<T> asyncFetch(Callable<T> task)
  {
    return pool.submit(task);
  }
 
  public Coordinate loadCoordinate(Photo photo) throws IOException
  {
    ExifFetchTask task = new ExifFetchTask(photo, this, StandardTag.GPS_LATITUDE, StandardTag.GPS_LONGITUDE, StandardTag.GPS_ALTITUDE);
    DerivedTask<ExifResult, Coordinate> dtask = new DerivedTask<>(task, v -> {
      if (!v.has(StandardTag.GPS_LATITUDE) || !v.has(StandardTag.GPS_LONGITUDE))
        return Coordinate.UNKNOWN;
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
    });
    
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

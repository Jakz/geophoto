package com.github.jakz.geophoto.reverse;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.BiConsumer;

import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.geocode.Geocode;
import com.github.jakz.geophoto.tools.CheckedThreadPoolExecutor;

public class GeoReversePool
{
  private final CheckedThreadPoolExecutor pool;
  private final GeocodeReverser reverser;

  public GeoReversePool(GeocodeReverser reverser) { this(reverser, 2); }
  public GeoReversePool(GeocodeReverser reverser, int poolSize)
  {
    this.reverser = reverser;
    pool = new CheckedThreadPoolExecutor(poolSize);
  }
  
  public Future<Geocode> submit(final Photo photo)
  {
    return pool.submit(new GeoReverseTask(reverser, photo.coordinate()));
  }
  
  public void submit(final Photo photo, final BiConsumer<Photo, Geocode> callback)
  {
    pool.submit(() -> {
      Geocode geocode = reverser.reverse(photo.coordinate());
      callback.accept(photo, geocode);
    });
  }
}

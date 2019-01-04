package com.github.jakz.geophoto;

import java.io.IOException;

import com.github.jakz.geophoto.cache.ThumbnailCache;
import com.github.jakz.geophoto.cache.db.PersistentDatabase;
import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.tools.Exif;
import com.pixbits.lib.util.ShutdownManager;

public class MyMediator implements Mediator
{
  PersistentDatabase database;
  ThumbnailCache thumbnailCache;
  Exif<Photo> exif;
  
  ShutdownManager shutdownManager;
  
  MyMediator()
  {
    database = new PersistentDatabase();
    thumbnailCache = new ThumbnailCache(5);
    exif = new Exif<>(5);
    
    shutdownManager = new ShutdownManager(true);
    shutdownManager.addTask(() -> {
      try
      {
        exif.dispose();
        database.shutdown();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }      
    });
  }
  
  @Override public void init() throws IOException
  {
    database.init();
  }
  
  @Override public void shutdown() throws IOException
  {
    StringBuilder dbStats = new StringBuilder();
    database.printStatistics(dbStats);
    System.out.println(dbStats.toString());
    
    database.shutdown();
  }
  
  @Override public PersistentDatabase pdatabase()
  {
    return database;
  }
  
  @Override public ThumbnailCache thumbnailCache()
  {
    return thumbnailCache;
  }
}

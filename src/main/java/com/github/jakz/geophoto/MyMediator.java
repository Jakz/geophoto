package com.github.jakz.geophoto;

import java.io.IOException;

import com.github.jakz.geophoto.cache.ThumbnailCache;
import com.github.jakz.geophoto.cache.db.PersistentDatabase;

public class MyMediator implements Mediator
{
  PersistentDatabase database;
  ThumbnailCache thumbnailCache;
  
  MyMediator()
  {
    database = new PersistentDatabase();
    thumbnailCache = new ThumbnailCache(5);
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

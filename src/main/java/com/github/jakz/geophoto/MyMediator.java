package com.github.jakz.geophoto;

import java.io.IOException;

import com.github.jakz.geophoto.cache.ThumbnailCache;
import com.github.jakz.geophoto.cache.db.PersistentDatabase;
import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.PhotoSearcher;
import com.github.jakz.geophoto.tools.Exif;
import com.github.jakz.geophoto.tools.PhotoScanner;
import com.github.jakz.geophoto.ui.UI;
import com.pixbits.lib.searcher.Searcher;
import com.pixbits.lib.util.ShutdownManager;

public class MyMediator implements Mediator
{
  UI ui;
  
  PersistentDatabase database;
  ThumbnailCache thumbnailCache;
  PhotoScanner scanner;
  Exif<Photo> exif;
  PhotoSearcher searcher;
  
  ShutdownManager shutdownManager;
  
  MyMediator()
  {
    database = new PersistentDatabase();
    thumbnailCache = new ThumbnailCache(5);
    scanner = new PhotoScanner();
    searcher = new PhotoSearcher();
    
    exif = new Exif<>(5);
    
    
    ui = new UI();
    
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
  
  @Override public Searcher<Photo> searcher() { return searcher; }
  @Override public PhotoScanner scanner() { return scanner; }
  
  @Override public PersistentDatabase pdatabase() { return database; }
  @Override public ThumbnailCache thumbnailCache() { return thumbnailCache; }
  
  
  @Override public UI ui() { return ui; }
}

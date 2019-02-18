package com.github.jakz.geophoto;

import java.io.IOException;

import com.github.jakz.geophoto.cache.ThumbnailCache;
import com.github.jakz.geophoto.cache.db.PersistentDatabase;
import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.tools.PhotoScanner;
import com.github.jakz.geophoto.ui.UI;
import com.pixbits.lib.searcher.Searcher;

public interface Mediator
{
  public void init() throws IOException;
  public void shutdown() throws IOException;
  
  public PhotoScanner scanner();
  public PersistentDatabase pdatabase();
  public ThumbnailCache thumbnailCache();
  
  public Searcher<Photo> searcher();
  
  public UI ui();
}
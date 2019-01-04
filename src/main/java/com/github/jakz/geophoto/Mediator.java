package com.github.jakz.geophoto;

import java.io.IOException;

import com.github.jakz.geophoto.cache.ThumbnailCache;
import com.github.jakz.geophoto.cache.db.PersistentDatabase;

public interface Mediator
{
  public void init() throws IOException;
  public void shutdown() throws IOException;
  
  public PersistentDatabase pdatabase();
  public ThumbnailCache thumbnailCache();
}
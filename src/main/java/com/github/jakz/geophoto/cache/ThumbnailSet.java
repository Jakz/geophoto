package com.github.jakz.geophoto.cache;

import java.io.IOException;
import java.util.function.BiConsumer;

import org.im4java.core.IM4JavaException;

import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.Size;

public class ThumbnailSet
{   
  private final Photo photo;
  private final boolean[] scheduled;
  private final Thumbnail[] thumbnails;
  
  public ThumbnailSet(Photo photo)
  {
    this.photo = photo;
    this.thumbnails = new Thumbnail[ThumbnailSize.count()];
    this.scheduled = new boolean[ThumbnailSize.count()];
  }
  
  public Thumbnail get(ThumbnailSize size)
  {
    return thumbnails[size.ordinal()];
  }
  
  public Thumbnail asyncGet(ThumbnailSize size, BiConsumer<Photo, Thumbnail> callback) throws IM4JavaException, InterruptedException, IOException
  {
    Thumbnail thumbnail = get(size);
    
    if (thumbnail != null)
      return thumbnail;
    else
    {
      if (scheduled[size.ordinal()])
        return null;
      
      scheduled[size.ordinal()] = true;
      
      ThumbnailCache.cache.getThumbnail(photo, new Size(80,80), (p,t) -> {
        thumbnails[size.ordinal()] = t;
        scheduled[size.ordinal()] = false;
        callback.accept(p,t);
      });
      
      return null;
    }
  }
}

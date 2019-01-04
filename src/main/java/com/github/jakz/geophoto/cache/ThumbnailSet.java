package com.github.jakz.geophoto.cache;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;

import org.im4java.core.IM4JavaException;

import com.github.jakz.geophoto.Mediator;
import com.github.jakz.geophoto.data.Photo;
import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.functional.TriConsumer;
import com.pixbits.lib.lang.Size;

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
  
  public Thumbnail asyncGet(Mediator mediator, ThumbnailSize size, TriConsumer<Photo, Thumbnail, Boolean> callback) throws IM4JavaException, InterruptedException, IOException
  {
    Thumbnail thumbnail = get(size);
    
    if (thumbnail != null)
      return thumbnail;
    else
    {
      if (scheduled[size.ordinal()])
        return null;
      
      Thumbnail tb = mediator.pdatabase().getThumbnailForPhoto(photo, size);
      
      if (tb != null)
      {
        thumbnails[size.ordinal()] = tb;
        callback.accept(photo, tb, false);
        return tb;
      }
      
      scheduled[size.ordinal()] = true;
      
      mediator.thumbnailCache().getThumbnail(photo, new Size.Int(80,80), StreamException.rethrowBiConsumer((p,t) -> {
        thumbnails[size.ordinal()] = t;
        scheduled[size.ordinal()] = false;
        
        mediator.pdatabase().setThumbnailForPhoto(photo, ThumbnailSize.TINY, t);
       
        callback.accept(p, t, true);
      }));
      
      return null;
    }
  }
  
  public long sizeInBytes()
  {
    return Arrays.stream(thumbnails)
        .filter(Objects::nonNull)
        .mapToLong(Thumbnail::sizeInBytes)
        .sum();
  }
}

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
import com.pixbits.lib.lang.Pair;
import com.pixbits.lib.lang.Size;

public class ThumbnailSet
{   
  private final Photo photo;
  private final boolean[] scheduled;
  private final Thumbnail[] thumbnails;
  
  public ThumbnailSet(Photo photo)
  {
    this.photo = photo;
    this.thumbnails = new Thumbnail[ThumbSize.count()];
    this.scheduled = new boolean[ThumbSize.count()];
  }
  
  public Thumbnail get(ThumbSize size)
  {
    return thumbnails[size.ordinal()];
  }
  
  public Pair<Thumbnail, Boolean> asyncGet(Mediator mediator, ThumbSize size, TriConsumer<Photo, Thumbnail, Boolean> callback) throws IM4JavaException, InterruptedException, IOException
  {
    Thumbnail thumbnail = get(size);
    
    if (thumbnail != null)
      return new Pair<>(thumbnail, false);
    else
    {
      if (scheduled[size.ordinal()])
        return new Pair<>(null, false);
      
      Thumbnail tb = mediator.pdatabase().getThumbnailForPhoto(photo, size);
      
      if (tb != null)
      {
        thumbnails[size.ordinal()] = tb;
        return new Pair<>(tb, false);
      }
      
      scheduled[size.ordinal()] = true;
      
      mediator.thumbnailCache().getThumbnail(photo, new Size.Int(size.size,size.size), StreamException.rethrowBiConsumer((p,t) -> {
        thumbnails[size.ordinal()] = t;
        scheduled[size.ordinal()] = false;
        
        mediator.pdatabase().setThumbnailForPhoto(photo, size, t);
       
        callback.accept(p, t, true);
      }));
      
      return new Pair<>(null, true);
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

package com.github.jakz.geophoto.cache;

import java.io.IOException;
import java.util.function.BiConsumer;

import org.im4java.core.IM4JavaException;

import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.tools.ImageMagick;
import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.lang.Size;

public class ThumbnailCache
{
  ImageMagick im;
  
  public ThumbnailCache(int poolSize)
  {
    im = new ImageMagick(8);
  }
  
  public void getThumbnail(Photo photo, Size.Int size, BiConsumer<Photo, Thumbnail> callback) throws IM4JavaException, InterruptedException, IOException
  {
    im.createThumbnail(photo, size, StreamException.rethrowBiConsumer((p,i) -> callback.accept(p, i)));
  }
  
  void dispose()
  {
    im.dispose();
  }
  
  void waitForAllTasks() throws InterruptedException
  {
    im.waitForAllTasks();
  }
}

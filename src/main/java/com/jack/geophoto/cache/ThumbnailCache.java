package com.jack.geophoto.cache;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.BiConsumer;

import org.im4java.core.IM4JavaException;

import com.jack.geophoto.data.Photo;
import com.jack.geophoto.data.Size;
import com.jack.geophoto.tools.ImageMagick;
import com.pixbits.lib.functional.StreamException;

public class ThumbnailCache
{
  public static final ThumbnailCache cache = new ThumbnailCache(5);

  ImageMagick im;
  
  ThumbnailCache(int poolSize)
  {
    im = new ImageMagick(8);
  }
  
  public void getThumbnail(Photo photo, Size size, BiConsumer<Photo, Thumbnail> callback) throws IM4JavaException, InterruptedException, IOException
  {
    im.createThumbnail(photo, new Size(80,80), StreamException.rethrowBiConsumer((p,i) -> callback.accept(p, i)));
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

package com.github.jakz.geophoto.tools;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

import com.github.jakz.geophoto.Log;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardFormat;

public class ExifFetchTask<T extends Exifable> implements Callable<ExifResult>
{
  private final Exif<T> exif;
  private final T photo;
  private final Tag[] tags;
  
  ExifFetchTask(T photo, Exif<T> exif, Tag... tags)
  {
    this.photo = photo;
    this.exif = exif;
    this.tags = tags;
  }
  
  @Override
  public ExifResult call() throws Exception
  {
    Log.d("exiftool", "executing ' exiftool...'");
    Map<Tag, String> values = exif.getTool().getImageMeta(photo.path().toFile(), StandardFormat.HUMAN_READABLE, Arrays.asList(tags));
    return new ExifResult(values);
  }

}

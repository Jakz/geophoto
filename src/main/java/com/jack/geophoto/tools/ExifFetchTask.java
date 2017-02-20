package com.jack.geophoto.tools;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.Callable;

import com.jack.geophoto.data.Photo;
import com.thebuzzmedia.exiftool.Tag;

public class ExifFetchTask implements Callable<ExifResult>
{
  private final Exif exif;
  private final Photo photo;
  private final Tag[] tags;
  
  ExifFetchTask(Photo photo, Exif exif, Tag... tags)
  {
    this.photo = photo;
    this.exif = exif;
    this.tags = tags;
  }
  
  @Override
  public ExifResult call() throws Exception
  {
    Map<Tag, String> values = exif.getTool().getImageMeta(photo.file(), Arrays.asList(tags));
    return new ExifResult(values);
  }

}

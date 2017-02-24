package com.github.jakz.geophoto.tools;

import java.util.function.BiConsumer;

import com.github.jakz.geophoto.data.Photo;

public class ExifConsumeTask implements Runnable
{
  private final Photo photo;
  private final BiConsumer<Photo, ExifResult> callback;
  private final ExifFetchTask task;
  
  ExifConsumeTask(Photo photo, ExifFetchTask task, BiConsumer<Photo, ExifResult> callback)
  {
    this.photo = photo;
    this.task = task;
    this.callback = callback;
  }
  
  @Override
  public void run()
  {
    try
    {
      ExifResult result = task.call();
      callback.accept(photo, result);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
}

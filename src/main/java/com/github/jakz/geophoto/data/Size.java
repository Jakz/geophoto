package com.github.jakz.geophoto.data;

import com.thebuzzmedia.exiftool.commons.lang.Objects;

public class Size
{
  public final int width;
  public final int height;
  
  public Size(int width, int height)
  {
    this.width = width;
    this.height = height;
  }
  
  @Override public boolean equals(Object o)
  {
    return o instanceof Size && ((Size)o).width == width && ((Size)o).height == height;
  }
  
  @Override public int hashCode()
  {
    return Objects.hashCode(width, height);
  }
  
  public Size scale(float percent)
  {
    return new Size((int)(width*percent), (int)(height*percent));
  }
}

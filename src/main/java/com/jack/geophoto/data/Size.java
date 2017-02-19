package com.jack.geophoto.data;

public class Size
{
  public final int width;
  public final int height;
  
  public Size(int width, int height)
  {
    this.width = width;
    this.height = height;
  }
  
  public Size scale(float percent)
  {
    return new Size((int)(width*percent), (int)(height*percent));
  }
}

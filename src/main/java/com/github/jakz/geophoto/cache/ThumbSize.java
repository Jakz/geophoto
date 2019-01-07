package com.github.jakz.geophoto.cache;

public enum ThumbSize
{
  TINY(100),
  SMALL(200),
  MEDIUM(400),
  LARGE(800)
  ;
  
  public int size;
  
  private ThumbSize(int size)
  {
    this.size = size;
  }
  
  public static int count()
  {
    return ThumbSize.values().length;
  }
}

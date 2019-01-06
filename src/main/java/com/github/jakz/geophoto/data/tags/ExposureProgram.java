package com.github.jakz.geophoto.data.tags;

public enum ExposureProgram
{
  UNDEFINED(0, "Undefined"),
  MANUAL(1, "Manual"),
  NORMAL(2, "Normal Program"),
  PRIORITY_APERTURE(3, "Aperture Priority"),
  PRIORITY_SHUTTER(4, "Shutter Priority"),
  CREATIVE_PROGRAM(5, "Creative Program"),
  ACTION_PROGRAM(6, "Action Program"),
  PORTRAIT_MODE(7, "Portrait Mode"),
  LANDSCAPE_MODE(8, "Landscape Mode"),
  
  OTHER(Integer.MAX_VALUE, "Other"),
  
  NONE(-1, "None")
  ;
  
  public final String caption;
  public final int index;
  
  private ExposureProgram(int index, String caption)
  {
    this.index = index;
    this.caption = caption;
  }
  
  public String toString() { return caption; }
  
  public static ExposureProgram get(int v)
  {
    return values()[v];
  }
}

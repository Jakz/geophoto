package com.github.jakz.geophoto.data;

import com.thebuzzmedia.exiftool.Tag;

public enum Tags implements Tag
{
  MaxApertureValue("MaxApertureValue"),
  ImageUniqueID("ImageUniqueID")
  ;
  
  private final String name;
  
  private Tags(String name)
  {
    this.name = name;
  }
  
  @Override public String getName() { return name; }
  @Override public String getDisplayName() { return name; }

  @Override
  public <T> T parse(String value)
  {
    return null;
  }

}

package com.github.jakz.geophoto.data;

import java.util.List;

public interface MutablePhotoEnumeration extends PhotoEnumeration
{
  public void add(Photo photo);
  
  public static MutablePhotoEnumeration of(List<Photo> photos, String title)
  {
    return new PhotoSet(photos)
    {
      @Override public String title() { return title; }
    };
  }
}

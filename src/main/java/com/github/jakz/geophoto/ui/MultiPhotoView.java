package com.github.jakz.geophoto.ui;

import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.PhotoEnumeration;

public interface MultiPhotoView
{
  public void selectPhoto(Photo photo);
  public void setPhotos(PhotoEnumeration photos);
}

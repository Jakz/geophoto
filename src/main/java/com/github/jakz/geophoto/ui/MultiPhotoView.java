package com.github.jakz.geophoto.ui;

import java.util.Optional;
import java.util.function.Predicate;

import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.PhotoEnumeration;

public interface MultiPhotoView
{
  public void selectPhoto(Photo photo);
  public void setPhotos(PhotoEnumeration photos);
  
  public void filter(Optional<Predicate<Photo>> filter);
}

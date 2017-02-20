package com.jack.geophoto.cache;

import com.jack.geophoto.data.Photo;

public class ThumbnailSet
{ 
  private final Photo photo;
  private final Thumbnail tiny;
  
  public ThumbnailSet(Photo photo)
  {
    this.photo = photo;
    tiny = new Thumbnail();
  }
  
  public Thumbnail tiny()
  {
    if (!tiny.isPresent())
      /* invoke async load */;
    
    return tiny;
  }
}

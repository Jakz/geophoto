package com.jack.geophoto.cache;

import java.awt.image.BufferedImage;

public class Thumbnail
{
  private BufferedImage image;
  
  Thumbnail()
  {
    this.image = null;
  }
  
  boolean isPresent() { return image != null; }
  
  public void setImage(BufferedImage image) { this.image = image; }
  public BufferedImage image() { return image; }
}

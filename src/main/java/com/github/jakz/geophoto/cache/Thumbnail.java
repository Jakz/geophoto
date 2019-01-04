package com.github.jakz.geophoto.cache;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;

public class Thumbnail
{
  private BufferedImage image;
    
  public Thumbnail(BufferedImage image)
  {
    this.image = image;
  }

  boolean isPresent() { return image != null; }
  
  public void setImage(BufferedImage image)
  { 
    this.image = image; 
  }
  
  public BufferedImage image() { return image; }
  
  public long sizeInBytes() { 
    DataBuffer buffer = image.getRaster().getDataBuffer();
    return buffer.getSize() * DataBuffer.getDataTypeSize(buffer.getDataType())/8;
  }

}

package com.github.jakz.geophoto.cache.db;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;

import org.fusesource.leveldbjni.*;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import com.github.jakz.geophoto.cache.Thumbnail;
import com.github.jakz.geophoto.cache.ThumbSize;
import com.github.jakz.geophoto.data.Photo;
import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.pixbits.lib.log.LogBuffer.Entry;

public class PersistentDatabase
{
  private static class Keys
  {
    static byte[] thumbnailKey(Photo photo, ThumbSize size)
    {
      return new StringBuilder().append(photo.path().toAbsolutePath().toString()).append("-thumb-").append(size.name()).toString().getBytes();
    }
    
    static byte[] coordinateKey(Photo photo)
    {
      return new StringBuilder().append(photo.path().toAbsolutePath().toString()).append("-coord").toString().getBytes();
    }
  }
  
  DB db;
  
  public void init() throws IOException
  {
    System.out.println("Opening database..");
    
    Options options = new Options();
    options.createIfMissing();
    
    db = JniDBFactory.factory.open(new File("db"), options);
  }
  
  public void shutdown() throws IOException
  {
    System.out.println("Saving database..");
    db.close();
  }
  
  
  public Thumbnail getThumbnailForPhoto(Photo photo, ThumbSize size) throws IOException
  {
    byte[] data = db.get(Keys.thumbnailKey(photo, size));
    
    if (data != null)
    {
      BufferedImage image = ImageIO.read(new ByteArrayInputStream(data));
      return new Thumbnail(image);
    }
    
    return null;
  }
  
  public void setThumbnailForPhoto(Photo photo, ThumbSize size, Thumbnail thumbnail) throws IOException
  {
    BufferedImage image = thumbnail.image();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(image, "jpg", baos);
    baos.flush();
    byte[] data = baos.toByteArray();
    
    db.put(Keys.thumbnailKey(photo, size), data);
  }
  
  public void setCoordinateForPhoto(Photo photo, Coordinate coord) throws IOException
  {
    db.put(Keys.coordinateKey(photo), coord.toByteArray());
  }
  
  public Coordinate getCoordinateForPhoto(Photo photo)
  {
    byte[] data = db.get(Keys.coordinateKey(photo));
    return data != null ? Coordinate.of(data) : null;
  }
  
  public void printStatistics(StringBuilder out)
  {
    DBIterator it = db.iterator();
    
    for(it.seekToFirst(); it.hasNext(); it.next())
    {
      Map.Entry<byte[], byte[]> entry = it.peekNext();
      
      out.append(new String(entry.getKey())).append(" : ").append(entry.getValue().length).append(" bytes\n");
    }
  }
}

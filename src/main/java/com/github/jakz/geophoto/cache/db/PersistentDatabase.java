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
import com.github.jakz.geophoto.data.attr.Attr;
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
    
    static byte[] attributeKey(Photo photo, Attr attr)
    {
      return new StringBuilder().append(photo.path().toAbsolutePath().toString()).append("-").append(attr.name()) .toString().getBytes();
    }
    
    static byte[] attributeCachedKey(Photo photo)
    {
      return photo.path().toString().getBytes();
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
  
  /* TODO: write last modified date as value so that can be used to automatically refresh photos if something is changed */
  public void markAttributesCachedForPhoto(Photo photo)
  {
    db.put(Keys.attributeCachedKey(photo), new byte[] { 1 });
  }
  
  public boolean areAttributesCachedForPhoto(Photo photo)
  {
    return db.get(Keys.attributeCachedKey(photo)) != null;
  }
  
  public void setAttributeForPhoto(Photo photo, Attr attr, Object value)
  {
    byte[] bytes = attr.toBytes(value);
    db.put(Keys.attributeKey(photo, attr), bytes);
  }
  
  public Object getAttributeForPhoto(Photo photo, Attr attr)
  {
    byte[] bytes = db.get(Keys.attributeKey(photo, attr));
    return bytes != null ? attr.fromBytes(bytes) : null;
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

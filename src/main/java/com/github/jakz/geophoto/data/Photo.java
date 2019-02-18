package com.github.jakz.geophoto.data;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Map;

import com.github.jakz.geophoto.Mediator;
import com.github.jakz.geophoto.cache.ThumbnailSet;
import com.github.jakz.geophoto.cache.db.PersistentDatabase;
import com.github.jakz.geophoto.data.attr.Attr;
import com.github.jakz.geophoto.data.attr.AttributeSet;
import com.github.jakz.geophoto.data.geocode.Geocode;
import com.github.jakz.geophoto.reverse.GeocodeReverser;
import com.github.jakz.geophoto.tools.Exifable;
import com.github.jakz.geophoto.ui.MarkerSource;
import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.pixbits.lib.lang.Size;

public class Photo implements Comparable<Photo>, MarkerSource, Exifable
{
  private Path path;  
  private final AttributeSet attrs;
  
  private String name;

  private Geocode geocode;
  
  
  private ThumbnailSet thumbnails;
  
  public Photo(Path path) throws NoSuchFileException
  {
    if (!Files.exists(path))
      throw new NoSuchFileException(path.toString());
    
    this.attrs = new AttributeSet();
    this.path = path;
    this.name = path.getFileName().toString();
    this.thumbnails = new ThumbnailSet(this);
  }

  public void save(PersistentDatabase db)
  {
    for (Map.Entry<Attr, Object> attr : attrs)
      if (attr.getValue() != null)
        db.setAttributeForPhoto(this, attr.getKey(), attr.getValue());
    
    db.markAttributesCachedForPhoto(this);
  }
  
  public boolean load(PersistentDatabase db)
  {
    if (db.areAttributesCachedForPhoto(this))
    {    
      for (Attr attr : Attr.values())
      {
        Object value = db.getAttributeForPhoto(this, attr);
        if (value != null)
          attrs.set(attr, value);
      }
      
      return true;
    }
    else
      return false;
  }
  
  @Override
  public int compareTo(Photo o) {
    return name.compareToIgnoreCase(o.name);
  }
  
  @Override
  public int hashCode()
  {
    return path.hashCode();
  }
  
  public void reverseGeoCode(GeocodeReverser reverser)
  {
    if (attrs.coordinate().isUnknown())
      geocode = Geocode.UNKNOWN;
    else if (attrs.coordinate() != null)
      geocode = reverser.reverse(attrs.coordinate());   
  }

  public void geocode(Geocode geocode) { this.geocode = geocode; } 
  
  public String filename() { return name; }
  public Path path() { return path; }
  public AttributeSet attrs() { return attrs; }
  
  public Geocode geocode() { return geocode; }
  
  public ThumbnailSet thumbnails() { return thumbnails; }
  
  
  public Coordinate coordinate() { return attrs.coordinate(); }
  public Integer iso() { return attrs.get(Attr.ISO); }
  public Rational exposureTime() { return attrs.get(Attr.EXPOSURE_TIME); }
  public Double fnumber() { return attrs.get(Attr.FNUMBER); }
  public Integer focalLength() { return attrs.get(Attr.FOCAL_LENGTH); }
  public LocalDateTime dateTimeOriginal() { return attrs.get(Attr.DATE_TIME_ORIGINAL); }
  
  public boolean isGeotagged() { return attrs.coordinate() != null && attrs.coordinate().isValid(); }
}

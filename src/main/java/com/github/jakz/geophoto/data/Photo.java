package com.github.jakz.geophoto.data;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import com.github.jakz.geophoto.cache.ThumbnailSet;
import com.github.jakz.geophoto.reverse.GeocodeReverser;
import com.github.jakz.geophoto.tools.Exifable;
import com.github.jakz.geophoto.ui.MarkerSource;
import com.pixbits.lib.lang.Size;

public class Photo implements Comparable<Photo>, MarkerSource, Exifable
{
  private String name;
  private Path path;
  private Coordinate coordinate;
  private Geocode geocode;
  private Size.Int size;
  
  private ThumbnailSet thumbnails;
  
  public Photo(Path path) throws NoSuchFileException
  {
    if (!Files.exists(path))
      throw new NoSuchFileException(path.toString());
    
    this.path = path;
    this.name = path.getFileName().toString();
    this.thumbnails = new ThumbnailSet(this);
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
    if (coordinate.isUnknown())
      geocode = Geocode.UNKNOWN;
    else if (coordinate != null)
      geocode = reverser.reverse(coordinate);   
  }

  public void geocode(Geocode geocode) { this.geocode = geocode; } 
  public void coordinate(Coordinate coord) { this.coordinate = coord; }
  
  public Path path() { return path; }
  public Coordinate coordinate() { return coordinate; }
  public Geocode geocode() { return geocode; }
  
  public ThumbnailSet thumbnails() { return thumbnails; }
}

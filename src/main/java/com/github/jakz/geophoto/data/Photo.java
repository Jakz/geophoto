package com.github.jakz.geophoto.data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import com.github.jakz.geophoto.cache.ThumbnailSet;
import com.github.jakz.geophoto.ui.MarkerSource;

public class Photo implements Comparable<Photo>, MarkerSource
{
  private String name;
  private Path path;
  private Coordinate coordinate;
  private Size size;
  
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
  
  public void coordinate(Coordinate coord) { this.coordinate = coord; }
  
  public Path path() { return path; }
  public File file() { return path.toFile(); }
  public Coordinate coordinate() { return coordinate; }
  
  public ThumbnailSet thumbnails() { return thumbnails; }
}

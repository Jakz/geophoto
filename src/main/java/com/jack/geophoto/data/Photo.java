package com.jack.geophoto.data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class Photo implements Comparable<Photo>
{
  private String name;
  private Path path;
  private Coordinate coordinate;
  private Size size;
  
  public Photo(Path path) throws NoSuchFileException
  {
    if (!Files.exists(path))
      throw new NoSuchFileException(path.toString());
    
    this.path = path;
    this.name = path.getFileName().toString();
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
}

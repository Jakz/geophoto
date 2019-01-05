package com.github.jakz.geophoto.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.pixbits.lib.io.FolderScanner;

public class PhotoFolder implements PhotoEnumeration, Comparable<PhotoFolder>
{  
  private final Path path;
  private final List<Photo> photos;
  
  public PhotoFolder(Path path, Collection<Photo> photos) throws NoSuchFileException
  {
    if (!Files.exists(path))
      throw new NoSuchFileException(path.toString());
    
    this.path = path;
    this.photos = new ArrayList<>(photos);
  }
  
  public List<Photo> photos() { return photos; }
  
  public void add(Photo photo)
  {
    photos.add(photo);
  }
  
  public void sort()
  {
    Collections.sort(photos);
  }
  
  @Override
  public int compareTo(PhotoFolder o)
  {
    return path.compareTo(o.path);
  }

  @Override
  public int size()
  {
    return photos.size();
  }

  @Override
  public Photo get(int index)
  {
    return photos.get(index);
  }

  @Override
  public Iterator<Photo> iterator()
  {
    return photos.iterator();
  }
  
  @Override
  public int indexOf(Photo object)
  {
    return photos.indexOf(object);
  }
  
  @Override public String title() { return path.getFileName().toString(); }
  
  public void forEach(Consumer<? super Photo> consumer)
  {
    photos.forEach(consumer);
  }
}

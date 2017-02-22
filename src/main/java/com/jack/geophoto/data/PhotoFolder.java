package com.jack.geophoto.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.pixbits.lib.io.FolderScanner;

public class PhotoFolder implements PhotoEnumeration, Comparable<PhotoFolder>
{
  static final FolderScanner scanner = new FolderScanner("glob:*.{JPG,jpg}", null, true);
  
  private Path path;
  private List<PhotoFolder> subFolders;
  private List<Photo> photos;
  private boolean recursive;
  
  public PhotoFolder(Path path, boolean recursive) throws NoSuchFileException
  {
    if (!Files.exists(path))
      throw new NoSuchFileException(path.toString());
    
    this.path = path;
    this.recursive = recursive;
    
   
    
    this.subFolders = new ArrayList<PhotoFolder>();
    this.photos = new ArrayList<Photo>();
  }
  
  public PhotoFolder(Path path) throws NoSuchFileException
  {
    this(path, true);
  }
  
  public void add(Photo photo)
  {
    photos.add(photo);
  }
  
  public void sort()
  {
    Collections.sort(photos);
    Collections.sort(subFolders);
  }
  
  public Set<Path> findAllImages() throws IOException
  {
    return scanner.scan(path);
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
  
  public void forEach(Consumer<? super Photo> consumer)
  {
    photos.forEach(consumer);
  }




}

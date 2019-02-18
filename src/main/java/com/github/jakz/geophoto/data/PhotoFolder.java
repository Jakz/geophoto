package com.github.jakz.geophoto.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.pixbits.lib.io.FolderScanner;
import com.pixbits.lib.ui.table.FilterableDataSource;
import com.pixbits.lib.ui.table.FilterableListDataSource;

public class PhotoFolder extends PhotoSet implements Comparable<PhotoFolder>
{  
  final Path path;
  
  public PhotoFolder(Path path, Collection<Photo> photos) throws NoSuchFileException
  {
    super(photos);
    
    if (!Files.exists(path))
      throw new NoSuchFileException(path.toString());
    
    this.path = path;
  }
  

  //TODO: wrong for super/subclasses
  @Override
  public int compareTo(PhotoFolder o)
  {
    return path.compareTo(o.path);
  }
  
  @Override public String title() { return path.getFileName().toString(); }
}

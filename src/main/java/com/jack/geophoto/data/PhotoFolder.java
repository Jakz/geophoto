package com.jack.geophoto.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import com.jack.geophoto.file.FolderScanner;

public class PhotoFolder
{
  static final FolderScanner scanner = new FolderScanner("glob:*.{JPG,jpg}", true);
  
  private Path path;
  private boolean recursive;
  
  public PhotoFolder(Path path, boolean recursive) throws NoSuchFileException
  {
    if (!Files.exists(path))
      throw new NoSuchFileException(path.toString());
    
    this.path = path;
    this.recursive = recursive;
  }
  
  public PhotoFolder(Path path) throws NoSuchFileException
  {
    this(path, true);
  }
  
  public Set<Path> findAllImages() throws IOException
  {
    return scanner.scan(path);
  }
}

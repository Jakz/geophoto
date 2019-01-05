package com.github.jakz.geophoto.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.github.jakz.geophoto.data.Photo;
import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.io.FolderScanner;

public class PhotoScanner
{
  public static enum Mode
  {
    NON_RECURSIVE,
    RECURSIVE,
    RECURSIVE_FLAT
  };
  
  private Mode mode;
  private final FolderScanner scanner;
  
  public PhotoScanner() { this(Mode.NON_RECURSIVE); }
  public PhotoScanner(Mode mode)
  {
    this.mode = mode;
    this.scanner = new FolderScanner("glob:*.{JPG,jpg}", null, mode != Mode.NON_RECURSIVE);
  }

  public Set<Photo> findAllPhotosInFolder(Path path) throws IOException
  {
    if (!Files.exists(path))
      throw new NoSuchFileException(path.toString());
    
    return scanner.scan(path).stream()
      .map(StreamException.rethrowFunction(p -> new Photo(p)))
      .collect(Collectors.toSet());
  }
}

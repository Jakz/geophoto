package com.github.jakz.geophoto.tools;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.github.jakz.geophoto.data.Photo;
import com.pixbits.lib.io.FolderScanner;

public class PhotoFinder
{
  public static enum Mode
  {
    NON_RECURSIVE,
    RECURSIVE,
    RECURSIVE_FLAT
  };
  
  private Mode mode;
  private final FolderScanner scanner;
  
  public PhotoFinder(Mode mode)
  {
    this.mode = mode;
    this.scanner = new FolderScanner("glob:*.{JPG,jpg}", null, mode != Mode.NON_RECURSIVE);
  }

  List<Photo> findAllPhotosInFolder(Path path)
  {
    if (!Files.exists(path))
      throw new NoSuchFileException(path.toString());

    return null;
  }
}

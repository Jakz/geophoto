package com.github.jakz.geophoto.data;

import java.util.Comparator;

public class PhotoSorter
{
  public static final Comparator<Photo> ByFileName = (p1, p2) -> p1.filename().compareToIgnoreCase(p2.filename());
}

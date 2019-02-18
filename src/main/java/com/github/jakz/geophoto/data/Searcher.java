package com.github.jakz.geophoto.data;

import com.pixbits.lib.searcher.SearchPredicate;

public class Searcher
{
  private static SearchPredicate<Photo> HasGpsPredicate = token -> token.equals("HasGps") ? Photo::isGeotagged : null;
  
}

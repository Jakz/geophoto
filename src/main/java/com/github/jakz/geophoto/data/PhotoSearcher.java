package com.github.jakz.geophoto.data;

import java.util.Arrays;

import com.pixbits.lib.searcher.BasicSearchParser;
import com.pixbits.lib.searcher.SearchPredicate;
import com.pixbits.lib.searcher.Searcher;

public class PhotoSearcher extends Searcher<Photo>
{
  private static final SearchPredicate<Photo> HasGpsPredicate = token -> token.equals("HasGps") ? Photo::isGeotagged : null;
  
  private static final SearchPredicate<Photo> IsoPredicate = token -> {
    if (token.startsWith("iso") & token.length() >= 5)
    {
      if (token.charAt(3) == '=')
      {
        try
        {
          int eiso = Integer.parseInt(token.substring(4));
          
          return photo -> {
            Integer iso = photo.iso();
            return iso != null && iso == eiso;
          };
        }
        catch (NumberFormatException e)
        {
          e.printStackTrace();
        }
      }
    }
    
    return null;
  };
  

  
  
  public PhotoSearcher()
  {
    super(new BasicSearchParser<Photo>(), Arrays.asList(HasGpsPredicate, IsoPredicate));
  }
}

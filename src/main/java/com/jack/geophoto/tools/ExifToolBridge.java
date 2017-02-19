package com.jack.geophoto.tools;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.jack.geophoto.data.Coordinate;
import com.jack.geophoto.data.Photo;
import com.thebuzzmedia.exiftool.ExifTool;
import com.thebuzzmedia.exiftool.ExifToolBuilder;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.StandardTag;
import com.thebuzzmedia.exiftool.exceptions.UnsupportedFeatureException;

import static java.util.Arrays.asList;

import java.io.IOException;

public class ExifToolBridge
{
  private ExifTool exifTool;
  
  public ExifToolBridge()
  {
    try 
    {
      exifTool = new ExifToolBuilder().withPath(Paths.get("./tools/exiftool").toFile())/*.enableStayOpen()*/.build();
    } 
    catch (UnsupportedFeatureException ex) 
    {
      exifTool = new ExifToolBuilder().build();
    }
  }
 
  public Coordinate loadCoordinate(Photo photo) throws IOException
  {
    Map<Tag, String> tags = exifTool.getImageMeta(photo.file(), asList(StandardTag.GPS_LATITUDE, StandardTag.GPS_LONGITUDE));
    
    tags.forEach((k, v) -> System.out.println("" + k + ": " + v + " - "+v.getClass().getName()));
    
    return new Coordinate(
        Double.parseDouble(tags.get(StandardTag.GPS_LATITUDE)), 
        Double.parseDouble(tags.get(StandardTag.GPS_LONGITUDE))
    );
  }
}

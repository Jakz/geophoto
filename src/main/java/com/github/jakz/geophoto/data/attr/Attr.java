package com.github.jakz.geophoto.data.attr;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.Function;

import com.github.jakz.geophoto.data.tags.ExposureProgram;
import com.github.jakz.geophoto.data.tags.Orientation;
import com.github.jakz.geophoto.tools.ExifResult;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.NonConvertedTag;
import com.thebuzzmedia.exiftool.core.StandardTag;


public enum Attr
{
  WIDTH(AttrParsers.IntParser, StandardTag.IMAGE_WIDTH),
  HEIGHT(AttrParsers.IntParser, StandardTag.IMAGE_HEIGHT),
  ORIENTATION(AttrParsers.EnumParser(Orientation.class), NonConvertedTag.of(StandardTag.ORIENTATION)),
  IMAGE_DESCRIPTION(AttrParsers.StringParser, Tags.ImageDescription),
  DATE_TIME_ORIGINAL(AttrParsers.DateParser, StandardTag.DATE_TIME_ORIGINAL),
  
  COORDINATE(AttrParsers.CoordinateParser, NonConvertedTag.of(StandardTag.GPS_LATITUDE), NonConvertedTag.of(StandardTag.GPS_LONGITUDE), NonConvertedTag.of(StandardTag.GPS_ALTITUDE)),
  
  ISO(AttrParsers.IntParser, StandardTag.ISO),
  EXPOSURE_TIME(AttrParsers.RationalParser, StandardTag.EXPOSURE_TIME),
  SHUTTER_SPEED(AttrParsers.RationalParser, StandardTag.SHUTTER_SPEED),
  FOCAL_LENGTH(AttrParsers.IntParser, NonConvertedTag.of(StandardTag.FOCAL_LENGTH)),
  FOCAL_LENGTH_35MM(AttrParsers.IntParser, NonConvertedTag.of(StandardTag.FOCAL_LENGTH_35MM)),

  APERTURE(AttrParsers.DoubleParser, StandardTag.APERTURE),
  MAX_APERTURE(AttrParsers.DoubleParser, Tags.MaxApertureValue),
  FNUMBER(AttrParsers.DoubleParser, StandardTag.FNUMBER),
  EXPOSURE_PROGRAM(AttrParsers.EnumParser(ExposureProgram.class), NonConvertedTag.of(StandardTag.EXPOSURE_PROGRAM)),
  
  CAMERA_MAKER(AttrParsers.StringParser, StandardTag.MAKE),
  CAMERA_MODEL(AttrParsers.StringParser, StandardTag.MODEL),
  LENS_MAKER(AttrParsers.StringParser, StandardTag.LENS_MAKE),
  LENS_MODEL(AttrParsers.StringParser, StandardTag.LENS_MODEL),
  
  UNIQUE_ID(AttrParsers.StringParser, Tags.ImageUniqueID)
  ;
  
  private final Tag[] tags;
  private Function<String[], ?> parser;
  
  private Attr(Function<String[], ?> parser, Tag... tags)
  {
    this.tags = tags;
    this.parser = parser;
  }
  
  public Object parse(ExifResult result)
  {
    String[] stags = Arrays.stream(tags).map(result::getString).toArray(i -> new String[i]);
    
    if (stags.length == 1 && stags[0] == null)
      return null;
    else
      return parser.apply(stags);
  }
  

  public int size() { return Attr.values().length; }
  
  private static Tag[] requiredTags; 
  public static Tag[] tags()
  {
    if (requiredTags == null)
    {
      requiredTags = Arrays.stream(values()).map(a -> a.tags)
          .flatMap(Arrays::stream)
          .toArray(i -> new Tag[i]);
    }
    
    return requiredTags;
  }
}

package com.github.jakz.geophoto.data.attr;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.github.jakz.geophoto.data.tags.ExposureProgram;
import com.github.jakz.geophoto.data.tags.Orientation;
import com.github.jakz.geophoto.tools.ExifResult;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.NonConvertedTag;
import com.thebuzzmedia.exiftool.core.StandardTag;


public enum Attr
{
  WIDTH(new AttrHandler.IntHandler(), StandardTag.IMAGE_WIDTH),
  HEIGHT(new AttrHandler.IntHandler(), StandardTag.IMAGE_HEIGHT),
  ORIENTATION(new AttrHandler.EnumHandler<>(Orientation.class), NonConvertedTag.of(StandardTag.ORIENTATION)),
  IMAGE_DESCRIPTION(new AttrHandler.StringHandler(), Tags.ImageDescription),
  DATE_TIME_ORIGINAL(new AttrHandler.DateHandler(), StandardTag.DATE_TIME_ORIGINAL),
  
  COORDINATE(new AttrHandler.CoordinateHandler(), NonConvertedTag.of(StandardTag.GPS_LATITUDE), NonConvertedTag.of(StandardTag.GPS_LONGITUDE), NonConvertedTag.of(StandardTag.GPS_ALTITUDE)),
  
  ISO(new AttrHandler.IntHandler(), StandardTag.ISO),
  EXPOSURE_TIME(new AttrHandler.RationalHandler(), StandardTag.EXPOSURE_TIME),
  SHUTTER_SPEED(new AttrHandler.RationalHandler(), StandardTag.SHUTTER_SPEED),
  FOCAL_LENGTH(new AttrHandler.IntHandler(), NonConvertedTag.of(StandardTag.FOCAL_LENGTH)),
  FOCAL_LENGTH_35MM(new AttrHandler.IntHandler(), NonConvertedTag.of(StandardTag.FOCAL_LENGTH_35MM)),

  APERTURE(new AttrHandler.DoubleHandler(), StandardTag.APERTURE),
  MAX_APERTURE(new AttrHandler.DoubleHandler(), Tags.MaxApertureValue),
  FNUMBER(new AttrHandler.DoubleHandler(), StandardTag.FNUMBER),
  EXPOSURE_PROGRAM(new AttrHandler.EnumHandler<>(ExposureProgram.class), NonConvertedTag.of(StandardTag.EXPOSURE_PROGRAM)),
  
  CAMERA_MAKER(new AttrHandler.StringHandler(), StandardTag.MAKE),
  CAMERA_MODEL(new AttrHandler.StringHandler(), StandardTag.MODEL),
  LENS_MAKER(new AttrHandler.StringHandler(), StandardTag.LENS_MAKE),
  LENS_MODEL(new AttrHandler.StringHandler(), StandardTag.LENS_MODEL),
  
  UNIQUE_ID(new AttrHandler.StringHandler(), Tags.ImageUniqueID),
  
  LOCATION_COUNTRY(new AttrHandler.StringHandler(), Tags.LocationCountry),
  LOCATION_CITY(new AttrHandler.StringHandler(), Tags.LocationCity),
  ;
  
  private final Tag[] tags;
  private AttrHandler<?> handler;

  
  private Attr(AttrHandler<?> handler, Tag... tags)
  {
    this.tags = tags;
    this.handler = handler;
  }
  
  public byte[] toBytes(Object value)
  {
    return handler.toBytesGeneric(value);
  }
  
  public Object fromBytes(byte[] buffer)
  {
    return handler.fromBytes(buffer);
  }
  
  public Object parse(ExifResult result)
  {
    String[] stags = Arrays.stream(tags).map(result::getString).toArray(i -> new String[i]);
    
    if (stags.length == 1 && stags[0] == null)
      return null;
    else
      return handler.parse(stags);
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

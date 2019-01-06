package com.github.jakz.geophoto.data.attr;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

import com.github.jakz.geophoto.data.Rational;
import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.thebuzzmedia.exiftool.core.StandardTag;

public class AttrParsers 
{
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
  
  public static Function<String[], LocalDateTime> DateParser = strings -> LocalDateTime.parse(strings[0], dateFormatter);
  public static Function<String[], Integer> IntParser = strings -> Integer.valueOf(strings[0]);
  public static Function<String[], Double> DoubleParser = strings -> Double.valueOf(strings[0]);

  public static Function<String[], Rational> RationalParser = strings -> new Rational(strings[0]);
  public static Function<String[], String> StringParser = strings -> strings[0];

  
  public static <T extends Enum<?>> Function<String[], T> EnumParser(final Class<T> type)
  {
    return strings -> {
      int ordinal = Integer.valueOf(strings[0]);
      return type.getEnumConstants()[ordinal];
    };
  }
  
  /* lat lng alt */
  public static Function<String[], Coordinate> CoordinateParser = strings -> 
  {
    double lat = strings[0] != null ? Double.parseDouble(strings[0]) : Double.NaN;
    double lng = strings[1] != null ? Double.parseDouble(strings[1]) : Double.NaN;
    double alt = strings[2] != null ? Double.parseDouble(strings[2]) : Double.NaN;
    
    if (Double.isNaN(lat) || Double.isNaN(lng))
      return new Coordinate(Double.NaN, Double.NaN);
    else
      return new Coordinate(lat, lng, alt);
  };
}

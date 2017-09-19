package com.github.jakz.geophoto.data;

import com.github.jakz.geophoto.tools.ExifResult;
import com.teamdev.jxmaps.LatLng;
import com.thebuzzmedia.exiftool.core.StandardTag;

public class Coordinate extends com.pixbits.lib.io.xml.gpx.Coordinate
{
  public Coordinate(double lat, double lng)
  {
    super(lat, lng);
  }
  
  public Coordinate(double lat, double lng, double alt)
  {
    super(lat, lng, alt);
  }
  
  public Coordinate(com.pixbits.lib.io.xml.gpx.Coordinate other)
  {
    super(other);
  }

  public Coordinate(LatLng latLng)
  {
    super(latLng.getLat(), latLng.getLng());
  }
  
  public static Coordinate parse(ExifResult v)
  {
    if (!v.has(StandardTag.GPS_LATITUDE) || !v.has(StandardTag.GPS_LONGITUDE))
      return new Coordinate(Double.NaN, Double.NaN);
    else
    {
      if (v.has(StandardTag.GPS_ALTITUDE))
        return new Coordinate(
            v.get(StandardTag.GPS_LATITUDE),
            v.get(StandardTag.GPS_LONGITUDE),
            v.get(StandardTag.GPS_ALTITUDE)
        );
      else
        return new Coordinate(
            v.get(StandardTag.GPS_LATITUDE),
            v.get(StandardTag.GPS_LONGITUDE)
        );             
    }
  }
  
  public LatLng toLatLng() { return new LatLng(lat(), lng()); }
}

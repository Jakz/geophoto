package com.jack.geophoto.data;

import java.util.Optional;

import com.jack.geophoto.tools.ExifResult;
import com.teamdev.jxmaps.LatLng;
import com.thebuzzmedia.exiftool.core.StandardTag;

public class Coordinate
{
  private double latitude;
  private double longitude;
  private double altitude;
  
  public Coordinate(double lat, double lng)
  {
    this(lat, lng, Double.NaN);
  }
  
  public Coordinate(double lat, double lng, double alt)
  {
    this.latitude = lat;
    this.longitude = lng;
    this.altitude = alt;
  }
  
  public static Coordinate parse(ExifResult v)
  {
    if (!v.has(StandardTag.GPS_LATITUDE) || !v.has(StandardTag.GPS_LONGITUDE))
      return Coordinate.UNKNOWN;
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
  
  public LatLng toLatLng() { return new LatLng(latitude, longitude); }
  
  public boolean isValid() { return true; }
  
  public double lat() { return latitude; }
  public double lng() { return longitude; }
  
  private static final double EARTH_RADIUS = 6371e3;
  
  public double haversineDistance(Coordinate other)
  {
    double fi1 = Math.toRadians(latitude);
    double fi2 = Math.toRadians(other.latitude);
    
    double deltafi = Math.toRadians(other.latitude - latitude);
    double deltalambda = Math.toRadians(other.longitude - longitude);
    
    double a = Math.sin(deltafi / 2) * Math.sin(deltafi / 2) +
        Math.cos(fi1) * Math.cos(fi2) *
        Math.sin(deltalambda / 2) * Math.sin(deltalambda / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
    return (EARTH_RADIUS * c) / 1000.0;
  }
  
  public double cosineDistance(Coordinate other)
  {
    double fi1 = Math.toRadians(latitude);
    double fi2 = Math.toRadians(other.latitude);
    
    double deltalambda = Math.toRadians(other.longitude - longitude);
    
    return (Math.acos(Math.sin(fi1)*Math.sin(fi2) + Math.cos(fi1)*Math.cos(fi2) * Math.cos(deltalambda)) * EARTH_RADIUS) / 1000.0;
  }
  
  public static final Coordinate UNKNOWN = new Coordinate(0.0, 0.0)
  {
     @Override public boolean equals(Object o) { return o == this; }
     @Override public String toString() { return "UNKNOWN"; }
     @Override public boolean isValid() { return false; }
  };
  
  @Override
  public boolean equals(Object o)
  {
    if (o instanceof Coordinate)
    {
      Coordinate c = (Coordinate)o;     
      return c != Coordinate.UNKNOWN && c.latitude == latitude && c.longitude == longitude;
    }
    else
      return false;
  }
  
  @Override
  public String toString()
  {
    return String.format("{ %2.4f, %2.4f }", latitude, longitude);
  }
}

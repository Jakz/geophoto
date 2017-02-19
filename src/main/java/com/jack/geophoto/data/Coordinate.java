package com.jack.geophoto.data;

public class Coordinate
{
  private double latitude;
  private double longitude;
  
  public Coordinate(double lat, double lng)
  {
    this.latitude = lat;
    this.longitude = lng;
  }
  
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
     public boolean equals(Object o) { return o == this; }
  };
  
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
}
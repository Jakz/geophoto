package com.github.jakz.geophoto.reverse;

import java.util.concurrent.Callable;

import com.github.jakz.geophoto.data.geocode.Geocode;
import com.pixbits.lib.io.xml.gpx.Coordinate;

public class GeoReverseTask implements Callable<Geocode>
{
  private final GeocodeReverser reverser;
  private final Coordinate coordinate;
  
  public GeoReverseTask(GeocodeReverser reverser, Coordinate coordinate)
  {
    this.reverser = reverser;
    this.coordinate = coordinate;
  }
  
  @Override
  public Geocode call() throws Exception
  {
    return reverser.reverse(coordinate);
  }

}

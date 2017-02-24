package com.github.jakz.geophoto.gpx;

import java.time.ZonedDateTime;

import com.github.jakz.geophoto.data.Coordinate;

public class GpxWaypoint
{
  Coordinate coordinate;
  ZonedDateTime time;
  
  public Coordinate coordinate() { return coordinate; }
  public ZonedDateTime time() { return time; }
}

package com.jack.geophoto.gpx;

import java.time.ZonedDateTime;

import com.jack.geophoto.data.Coordinate;

public class GpxWaypoint
{
  Coordinate coordinate;
  ZonedDateTime time;
  
  public Coordinate coordinate() { return coordinate; }
}

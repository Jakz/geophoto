package com.github.jakz.geophoto.gpx;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Gpx
{
  List<GpxTrack> tracks;
  String name;
  ZonedDateTime time;
  /*List<GpxWaypoint> waypoints;*/

 
  public Gpx()
  {
    tracks = new ArrayList<>();
    //waypoints = new ArrayList<>();
  }
  
  public List<GpxTrack> tracks() { return tracks; }
  public String name() { return name; }
  public ZonedDateTime time() { return time; }
}

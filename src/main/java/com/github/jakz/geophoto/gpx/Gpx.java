package com.github.jakz.geophoto.gpx;

import java.util.ArrayList;
import java.util.List;

public class Gpx
{
  List<GpxTrack> tracks;
  /*List<GpxWaypoint> waypoints;*/

 
  public Gpx()
  {
    tracks = new ArrayList<>();
    //waypoints = new ArrayList<>();
  }
  
  public List<GpxTrack> tracks() { return tracks; }
}

package com.jack.geophoto.gpx;

import java.util.ArrayList;
import java.util.List;

public class GpxTrack
{
  String name;
  String comment;
  String description;
  String source;
  int trackNumber;
  List<GpxTrackSegment> segments;
  
  GpxTrack()
  {
    segments = new ArrayList<>();
  }
  
  public List<GpxTrackSegment> segments() { return segments; }
}

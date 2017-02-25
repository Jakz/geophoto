package com.github.jakz.geophoto.gpx;

import java.time.ZonedDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.github.jakz.geophoto.data.Coordinate;

@XmlJavaTypeAdapter(GpxWaypointAdapter.class)
public class GpxWaypoint
{
  Coordinate coordinate;
  ZonedDateTime time;
  GpxExtension extensions;
 
  public Coordinate coordinate() { return coordinate; }
  public ZonedDateTime time() { return time; }
}

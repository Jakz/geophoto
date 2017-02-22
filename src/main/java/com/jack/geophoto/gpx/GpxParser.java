package com.jack.geophoto.gpx;

import java.io.IOException;
import java.nio.file.Path;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.jack.geophoto.data.Coordinate;
import com.pixbits.lib.io.xml.XMLHandler;
import com.pixbits.lib.io.xml.XMLParser;

public class GpxParser extends XMLHandler<Gpx>
{
  Gpx gpx;
  GpxTrack track;
  GpxTrackSegment segment;
  GpxWaypoint waypoint;
  boolean stillInHeader;

  @Override
  protected void init()
  {
    gpx = new Gpx();
    stillInHeader = true;
  }
  
  @Override
  protected void start(String name, Attributes attr)
  {
    if (name.equals("gpx"))
      gpx = new Gpx();
    else if (name.equals("trk"))
      track = new GpxTrack();
    else if (name.equals("trkseg"))
      segment = new GpxTrackSegment();
    else if (name.equals("trkpt"))
    {
      waypoint = new GpxWaypoint();
      double latitude = this.getDoubleAttribute("lat", d -> d >= -90.f && d < 90.0f);
      double longitude = this.getDoubleAttribute("lon", d -> d >= -180.f && d < 180.0f);
      waypoint.coordinate = new Coordinate(latitude, longitude);
    }
    
  }

  @Override
  protected void end(String name)
  {
    if (name.equals("trkpt"))
      segment.points.add(waypoint);
    else if (name.equals("trkseg"))
      track.segments.add(segment);
    else if (name.equals("trk"))
      gpx.tracks.add(track);
    else if (name.equals("time") && !stillInHeader)
      waypoint.time = asZonedDateTime();
    else if (name.equals("metadata"))
      stillInHeader = false;
  }

  @Override
  public Gpx get()
  {
    return gpx;
  }

  public static Gpx parse(Path path) throws IOException, SAXException
  {
    GpxParser handler = new GpxParser();
    XMLParser<Gpx> gpxParser = new XMLParser<>(handler);
    Gpx gpx = gpxParser.load(path);
    return gpx;
  }
}

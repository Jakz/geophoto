package com.github.jakz.geophoto.gpx;

import java.io.IOException;
import java.nio.file.Path;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.github.jakz.geophoto.data.Coordinate;
import com.pixbits.lib.io.xml.XMLHandler;
import com.pixbits.lib.io.xml.XMLParser;

public class GpxParser extends XMLHandler<Gpx>
{
  Gpx gpx;
  GpxTrack track;
  GpxTrackSegment segment;
  GpxWaypoint waypoint;
  boolean inMetadata;

  @Override
  protected void init()
  {
    gpx = new Gpx();
    inMetadata = false;
  }
  
  @Override
  protected void start(String name, Attributes attr)
  {
    if (name.equals("metadata"))
      inMetadata = true;
    else if (name.equals("gpx"))
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
    if (name.equals("metadata"))
      inMetadata = false;
    else if (name.equals("trkpt"))
    {
      segment.points.add(waypoint);
      waypoint = null;
    }
    else if (name.equals("trkseg"))
    {
      track.segments.add(segment);
      segment = null;
    }
    else if (name.equals("trk"))
    {
      gpx.tracks.add(track);
      track = null;
    }
    else if (name.equals("ele"))
      waypoint.coordinate.setAlt(asDouble());
    else if (name.equals("name"))
    {
      if (inMetadata)
        gpx.name = asString();
      else if (track != null)
        track.name = asString();
    }
    else if (name.equals("time"))
    {
      if (inMetadata)
        gpx.time = asZonedDateTime();
      else
        waypoint.time = asZonedDateTime();
    }    
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

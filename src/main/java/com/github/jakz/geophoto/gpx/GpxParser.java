package com.github.jakz.geophoto.gpx;

import java.io.IOException;
import java.nio.file.Path;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.github.jakz.geophoto.data.Coordinate;
import com.pixbits.lib.io.xml.XMLHandler;
import com.pixbits.lib.io.xml.XMLParser;
import com.pixbits.lib.io.xml.XMLScopedHandler;

public class GpxParser extends XMLScopedHandler<Gpx>
{
  Gpx gpx;
  GpxTrack track;
  GpxTrackSegment segment;
  GpxWaypoint waypoint;

  @Override
  protected void init()
  {
    gpx = new Gpx();
  }
  
  @Override
  protected void start(String name, Attributes attr) throws SAXException
  {
    switch (name)
    {
      case "gpx": gpx = new Gpx(); break;
      case "trk": track = new GpxTrack(); break;
      case "trkseg": segment = new GpxTrackSegment(); break;
      case "trkpt":
      {
        waypoint = new GpxWaypoint();
        double latitude = this.getDoubleAttribute("lat", d -> d >= -90.f && d < 90.0f);
        double longitude = this.getDoubleAttribute("lon", d -> d >= -180.f && d < 180.0f);
        waypoint.coordinate = new Coordinate(latitude, longitude);
        break;
      }
    }  
  }

  @Override
  protected void end(String name) throws SAXException
  {
    switch (name)
    {
      case "trkpt": 
      {
        segment.points.add(waypoint);
        waypoint = null;
        break;
      }
      case "trkseg":
      {
        track.segments.add(segment);
        segment = null;
        break;
      }
      case "trk":
      {
        gpx.tracks.add(track);
        track = null;
      }
      case "ele":
      {
        assertCurrentScope("trkpt");
        waypoint.coordinate.setAlt(asDouble());
        break;
      }
      case "name":
      {
        if (isCurrentScope("metadata"))
        {
          gpx.name = asString();
        }
        else if (isCurrentScope("trk"))
        {
          track.name = asString();
        }
        
        break;
      }
      case "time":
      {
        if (isCurrentScope("metadata"))
          gpx.time = asZonedDateTime();
        else if (isCurrentScope("trkpt"))
          waypoint.time = asZonedDateTime();
        break;
      }
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

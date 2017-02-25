package com.github.jakz.geophoto.gpx;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class GpxTrack
{
  @XmlElement String name;
  @XmlElement(name = "cmt") String comment;
  @XmlElement(name = "desc") String description;
  @XmlElement(name = "src") String source;
  @XmlElement(name = "type") String type;
  @XmlElement(name = "link") List<GpxLink> links;
  @XmlElement(name = "number") Integer trackNumber;
  @XmlElement GpxExtension extensions;
  @XmlElement(name = "trkseg") List<GpxTrackSegment> segments;
  
  GpxTrack()
  {

  }
  
  public List<GpxTrackSegment> segments() { return segments; }
  public String name() { return name; }
}

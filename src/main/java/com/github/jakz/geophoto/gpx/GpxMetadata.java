package com.github.jakz.geophoto.gpx;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GpxMetadata
{
  @XmlElement String name;
  @XmlElement(name = "desc") String description;
  @XmlElement GpxPerson author;
  @XmlElement GpxCopyright copyright;
  @XmlElement(name = "link") List<GpxLink> links;
  @XmlElement GpxTime time;
  @XmlElement String keywords;
  @XmlElement GpxBounds bounds;
  @XmlElement GpxExtension extensions;
  
  GpxMetadata()
  {
  }
}

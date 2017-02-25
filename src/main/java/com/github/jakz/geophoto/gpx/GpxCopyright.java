package com.github.jakz.geophoto.gpx;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class GpxCopyright
{
  @XmlAttribute String author;
  @XmlElement int year;
  @XmlElement String license;
}

package com.github.jakz.geophoto.gpx;

import javax.xml.bind.annotation.XmlElement;

public class GpxPerson
{
  @XmlElement String name;
  @XmlElement String email;
  @XmlElement GpxLink link;
}

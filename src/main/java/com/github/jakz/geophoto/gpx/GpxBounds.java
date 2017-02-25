package com.github.jakz.geophoto.gpx;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAttribute;

import com.github.jakz.geophoto.data.Coordinate;

public class GpxBounds
{
  @XmlAttribute BigDecimal minlat;
  @XmlAttribute BigDecimal minlon;
  @XmlAttribute BigDecimal maxlat;
  @XmlAttribute BigDecimal maxlon;
}

package com.github.jakz.geophoto.reverse;

import com.github.jakz.geophoto.data.geocode.Geocode;
import com.pixbits.lib.io.xml.gpx.Coordinate;

public interface GeocodeReverser
{
  Geocode reverse(Coordinate coordinate);
}

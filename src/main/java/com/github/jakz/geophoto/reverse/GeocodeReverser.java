package com.github.jakz.geophoto.reverse;

import com.github.jakz.geophoto.data.Coordinate;
import com.github.jakz.geophoto.data.Geocode;

public interface GeocodeReverser
{
  Geocode reverse(Coordinate coordinate);
}

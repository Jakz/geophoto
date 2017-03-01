package com.github.jakz.geophoto.ui.map.builders;

import com.github.jakz.geophoto.ui.map.MapMarker;
import com.teamdev.jxmaps.Map;

public class MarkerBuilder extends MapElementBuilder<MapMarker>
{
  MarkerBuilder(Map map) { super(map); }
  
  @Override
  public MapMarker build()
  {
    return new MapMarker(map);
  }
}

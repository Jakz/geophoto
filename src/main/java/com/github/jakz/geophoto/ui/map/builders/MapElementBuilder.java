package com.github.jakz.geophoto.ui.map.builders;

import com.github.jakz.geophoto.ui.map.MapElement;
import com.teamdev.jxmaps.Map;

public abstract class MapElementBuilder<T extends MapElement>
{
  final Map map;
  MapElementBuilder(Map map) { this.map = map; }
  
  abstract public T build();
}

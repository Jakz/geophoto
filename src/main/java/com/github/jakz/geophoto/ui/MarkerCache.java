package com.github.jakz.geophoto.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.github.jakz.geophoto.data.Coordinate;

public class MarkerCache
{ 
  private final com.teamdev.jxmaps.Map map;
  private final Set<Marker<?>> markers;
  private final Map<Object, Marker<?>> markersMap;

  MarkerCache(com.teamdev.jxmaps.Map map)
  {
    this.map = map;
    this.markers = new HashSet<>();
    this.markersMap = new HashMap<>(); 
  }
  
  public <T extends MarkerSource> void addMarker(T owner, Coordinate position)
  {
    Marker<T> marker = new Marker<>(map, position, owner);
    markers.add(marker);
    markersMap.put(owner, marker);
  }
  
  public void addMarker(Coordinate position)
  {
    Marker<?> marker = new Marker<>(map, position);
    markers.add(marker);
  }

  public void clearMarkers()
  {
    markers.forEach(m -> m.remove());
    markers.clear();
    markersMap.clear();
  }

  public Set<Marker<?>> markers() { return markers; }
  
  public static class ID
  {
    private final MarkerCache cache;
    private final Marker<?> marker;
    
    private ID(MarkerCache cache, Marker<?> marker)
    {
      this.cache = cache;
      this.marker = marker;
    }
  }
}

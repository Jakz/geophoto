package com.jack.geophoto.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jack.geophoto.data.Coordinate;
import com.teamdev.jxmaps.Marker;

public class MarkerCache
{ 
  private final com.teamdev.jxmaps.Map map;
  private final List<Marker> markers;
  private final Map<Object, Marker> markersMap;
  
  MarkerCache(com.teamdev.jxmaps.Map map)
  {
    this.map = map;
    this.markers = new ArrayList<>();
    this.markersMap = new HashMap<>(); 
  }
  
  public void addMarker(Object ref, Coordinate position)
  {
    Marker marker = new Marker(map);
    marker.setPosition(position.toLatLng());
    markersMap.put(ref, marker);
  }
  
  public void addOrphanMarker(Coordinate position)
  {
    System.out.println("adding marker at "+position);
    
    Marker marker = new Marker(map);
    marker.setPosition(position.toLatLng());
    markers.add(marker);
  }
  
  public void clearAllMarkers()
  {
    clearMappedMarkers();
    clearOrphanMarkers();
  }
  
  public void clearMappedMarkers()
  {
    markersMap.forEach((k,m) -> m.remove());
    markersMap.clear();
  }
  
  public void clearOrphanMarkers()
  {
    markers.forEach(m -> m.remove());
    markers.clear();
  }
  
  public static class ID
  {
    private final MarkerCache cache;
    private final Marker marker;
    
    private ID(MarkerCache cache, Marker marker)
    {
      this.cache = cache;
      this.marker = marker;
    }
  }
}

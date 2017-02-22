package com.jack.geophoto.ui.map;

import java.util.Collection;
import java.util.stream.Collectors;

import com.jack.geophoto.data.Coordinate;
import com.jack.geophoto.gpx.GpxTrackSegment;
import com.jack.geophoto.gpx.GpxWaypoint;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MouseEvent;
import com.teamdev.jxmaps.Polyline;
import com.teamdev.jxmaps.PolylineOptions;

public class GpsTrackLine extends Polyline implements Positionable
{
  private final GpxTrackSegment track;
  private final Map map;
  
  public GpsTrackLine(GpxTrackSegment track, Map map)
  {
    super(map);
    this.track = track;
    this.map = map;
    
    LatLng[] points = track.points().stream()
      .map(w -> w.coordinate())
      .map(Coordinate::toLatLng)
      .toArray(i -> new LatLng[i]);
    
    setPath(points);

    PolylineOptions options = new PolylineOptions();
    options.setGeodesic(true);
    options.setStrokeColor("#FF0000");
    options.setStrokeOpacity(1.0);
    options.setStrokeWeight(2.0);
    setOptions(options);
    
    this.addEventListener("click", new MapMouseEvent() {
      @Override public void onEvent(MouseEvent event) { onClick(event); }
    });
  }
  
  private void onClick(MouseEvent event)
  {
    Coordinate coordinate = new Coordinate(event.latLng());
    Coordinate nearest = findNearestPointTo(coordinate);
  }
  
  public Map map() { return map; }
  
  public Collection<Coordinate> coordinates() 
  { 
    return track.points().stream()
      .map(GpxWaypoint::coordinate)
      .collect(Collectors.toList());
  }
}

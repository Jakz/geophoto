package com.github.jakz.geophoto.ui.map;

import java.awt.Color;
import java.util.Collection;
import java.util.stream.Collectors;

import com.github.jakz.geophoto.data.Coordinate;
import com.github.jakz.geophoto.gpx.GpxTrackSegment;
import com.github.jakz.geophoto.gpx.GpxWaypoint;
import com.pixbits.lib.ui.color.ColorGenerator;
import com.pixbits.lib.ui.color.ColorUtils;
import com.pixbits.lib.ui.color.PleasantColorGenerator;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MouseEvent;
import com.teamdev.jxmaps.Polyline;
import com.teamdev.jxmaps.PolylineOptions;

public class GpsTrackLine extends Polyline implements MapElement
{  
  private GpxTrackSegment track;
  private final Map map;
  
  private Color color;
  private float opacity;
  private float weight;
  
  public GpsTrackLine(Map map)
  {
    super(map);
    this.map = map;
    
    setVisible(false);
   
    opacity = 1.0f;
    weight = 2.0f;
    
    this.addEventListener("click", new MapMouseEvent() {
      @Override public void onEvent(MouseEvent event) { onClick(event); }
    });
  }
  
  public void setSegment(GpxTrackSegment track)
  {
    this.track = track;
    this.color = track.color();
    
    rebuild();
  }
  
  private void rebuild()
  {    
    LatLng[] points = track.points().stream()
        .map(w -> w.coordinate())
        .map(Coordinate::toLatLng)
        .toArray(i -> new LatLng[i]);
      
    setPath(points);
      
    PolylineOptions options = new PolylineOptions();
    options.setGeodesic(true);
    
    options.setStrokeColor(ColorUtils.colorToHex(color));
    options.setStrokeOpacity(opacity);
    options.setStrokeWeight(weight);
    setOptions(options);
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
  
  @Override 
  public void remove()
  {
    /* TODO */
  }

  @Override
  public void hide()
  {
    setVisible(false);
    
  }

  @Override
  public void show()
  {
    setVisible(true);
  }
}

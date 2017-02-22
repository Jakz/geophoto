package com.jack.geophoto.ui.map;

import java.util.Collection;

import com.jack.geophoto.data.Bounds;
import com.jack.geophoto.data.Coordinate;
import com.teamdev.jxmaps.Map;

public interface Positionable
{
  Map map();
  Collection<Coordinate> coordinates();
  
  default void centerAndFit()
  {
    Collection<Coordinate> coordinates = coordinates();
    
    Coordinate center = Coordinate.computeCenterOfGravity(coordinates);
    map().setCenter(center.toLatLng());
    double zoomLevel = getBoundsZoomLevel(coordinates, 800, 800);
    map().setZoom(zoomLevel-1);
  }
  
  default double zoom(double mapPx, double worldPx, double fraction)
  {
    return Math.floor(Math.log(mapPx / worldPx / fraction) / Math.log(2));
  }
  
  default double latRad(double lat)
  {
    double sin = Math.sin(lat * Math.PI / 180);
    double radX2 = Math.log((1 + sin) / (1 - sin)) / 2;
    return Math.max(Math.min(radX2, Math.PI), -Math.PI) / 2;
  }
  
  default double getBoundsZoomLevel(Collection<Coordinate> coords, float mwidth, float mheight)
  {
    final float width = 256, height = 256;
    final float zoomMax = 21;
    
    Bounds bound = new Bounds(coords);
    Coordinate ne = bound.ne(), sw = bound.sw();

    double latFraction = (latRad(ne.lat()) - latRad(sw.lat())) / Math.PI;
    
    double lngDiff = ne.lng() - sw.lng();
    double lngFraction = ((lngDiff < 0) ? (lngDiff + 360) : lngDiff) / 360;
    
    double latZoom = zoom(mheight, height, latFraction);
    double lngZoom = zoom(mwidth, width, lngFraction);
    
    double zoom = Math.min(Math.min(latZoom, lngZoom), zoomMax);
    
    System.out.println("NE: "+ne+", SW: "+sw+" ZOOM: "+zoom);

    return zoom;
  }
}

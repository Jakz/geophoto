package com.jack.geophoto.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.xml.sax.SAXException;

import com.jack.geophoto.data.Bounds;
import com.jack.geophoto.data.Coordinate;
import com.jack.geophoto.gpx.Gpx;
import com.jack.geophoto.gpx.GpxParser;
import com.jack.geophoto.gpx.GpxTrackSegment;
import com.jack.geophoto.ui.map.GpsTrackLine;
import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.MapViewOptions;
import com.teamdev.jxmaps.swing.MapView;

public class MapPanel extends JPanel
{
  private final MapView view;
  public Map map; // TODO: should be private
  private MarkerCache markers;
  private boolean ready;
  
  private List<Coordinate> unreadyBuffer = new ArrayList<>();
  
  public MapPanel() 
  {   
    ready = false;
    MapViewOptions viewOptions = new MapViewOptions();
    viewOptions.setApiKey("AIzaSyDZgDPjmu4dI-izRMQFzTX5Qpa2aWFirgo");
    
    view = new MapView(viewOptions);
    view.setOnMapReadyHandler(status -> {
      // Check if the map is loaded correctly
      if (status == MapStatus.MAP_STATUS_OK) 
      {
        map = view.getMap();
        markers = new MarkerCache(map);
        
        MapTypeControlOptions controlOptions = new MapTypeControlOptions();
        controlOptions.setPosition(ControlPosition.TOP_RIGHT);
        
        MapOptions options = new MapOptions();
        options.setMapTypeControlOptions(controlOptions);        
        map.setOptions(options);
    
        map.setCenter(new LatLng(35.91466, 10.312499));
        map.setZoom(10.0);
        
        ready = true;
        
        if (!unreadyBuffer.isEmpty())
        {
          unreadyBuffer.forEach(p -> markers.addMarker(p));
          unreadyBuffer.clear();
        }
        
        Gpx gpx;
        try
        {
          gpx = GpxParser.parse(Paths.get("./photos/data.gpx"));
          UI.gpxPanel.setTracks(gpx.tracks());          
        } 
        catch (IOException | SAXException e)
        {
          e.printStackTrace();
        }       
      }
    });
    
    setPreferredSize(new Dimension(800,800));
    setLayout(new BorderLayout());
    add(view, BorderLayout.CENTER);
  }
  
  public MarkerCache markers() { return markers; }
  
  private double zoom(double mapPx, double worldPx, double fraction)
  {
    return Math.floor(Math.log(mapPx / worldPx / fraction) / Math.log(2));
  }
  
  private double latRad(double lat)
  {
    double sin = Math.sin(lat * Math.PI / 180);
    double radX2 = Math.log((1 + sin) / (1 - sin)) / 2;
    return Math.max(Math.min(radX2, Math.PI), -Math.PI) / 2;
  }
  
  double getBoundsZoomLevel(Collection<Coordinate> coords, float mwidth, float mheight)
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
    
  void fitMarkers(Collection<Marker<?>> markers)
  {
    if (markers.isEmpty())
      return;
    
    Set<Coordinate> coords = markers.stream().map(Marker::coordinate).collect(Collectors.toSet());
    Coordinate center = Coordinate.computeCenterOfGravity(coords);
    
    map.setCenter(center.toLatLng());
    double zoomLevel = getBoundsZoomLevel(coords, 800, 800);
    map.setZoom(zoomLevel-1);
  }
  
  public void addMarker(final Coordinate position)
  {
    SwingUtilities.invokeLater(() -> 
    {
      if (ready)
        markers.addMarker(position);
      else
        unreadyBuffer.add(position);
    });
  }
}

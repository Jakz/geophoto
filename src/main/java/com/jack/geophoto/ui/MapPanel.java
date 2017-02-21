package com.jack.geophoto.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.jack.geophoto.data.Coordinate;
import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.MapViewOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.swing.MapView;

public class MapPanel extends JPanel
{
  private final MapView view;
  private Map map;
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
        map.setZoom(2.0);
        
        ready = true;
        
        if (!unreadyBuffer.isEmpty())
        {
          unreadyBuffer.forEach(p -> markers.addOrphanMarker(p));
          unreadyBuffer.clear();
        }
      }
    });
    
    setPreferredSize(new Dimension(800,800));
    setLayout(new BorderLayout());
    add(view, BorderLayout.CENTER);
  }
  
  public MarkerCache markers() { return markers; }
  
  public void addMarker(final Coordinate position)
  {
    SwingUtilities.invokeLater(() -> 
    {
      if (ready)
        markers.addOrphanMarker(position);
      else
        unreadyBuffer.add(position);
    });
  }
}

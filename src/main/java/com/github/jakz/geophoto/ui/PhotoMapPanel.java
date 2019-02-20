package com.github.jakz.geophoto.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.viewer.TileFactoryInfo;

import com.github.jakz.geophoto.data.Photo;
import com.pixbits.lib.io.xml.gpx.Bounds;
import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.pixbits.lib.ui.map.MapPanel;
import com.pixbits.lib.ui.map.Marker;
import com.pixbits.lib.ui.map.MarkerPainter;

public class PhotoMapPanel extends JPanel
{
  private enum MapMode
  {
    OPEN_STREET_MAP("OpenStreetMap (map)", new OSMTileFactoryInfo()),
    VIRUTAL_EARTH_MAP("VirtualEarth (map)", new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP)),
    VIRUTAL_EARTH_SATELLITE("VirtualEarth (satellite)", new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE)),
    VIRUTAL_EARTH_HYBRID("VirtualEarth (hybrid)", new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID))
    ;
    
    public final String name;
    public final TileFactoryInfo info;
    
    MapMode(String name, TileFactoryInfo info)
    {
      this.name = name;
      this.info = info;
    }
    
    @Override public String toString() { return name; }
  }
  
  private MapPanel map;
  
  private JPanel topPanel;
  private JComboBox<MapMode> mapMode;
  
  MarkerPainter<Photo> markerPainter = new MarkerPainter<Photo>();
  
  public PhotoMapPanel()
  {
    setLayout(new BorderLayout());

    map = new MapPanel(600,600);
    map.addPainter(markerPainter);
    map.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY));
    add(map, BorderLayout.CENTER);
    
    topPanel = new JPanel();
    
    mapMode = new JComboBox<>(MapMode.values());
    mapMode.addItemListener(e -> {
      if (e.getStateChange() == ItemEvent.SELECTED) 
        map.setTileFactoryInfo(mapMode.getItemAt(mapMode.getSelectedIndex()).info);
    });
    
    topPanel.add(mapMode);
    
    add(topPanel, BorderLayout.NORTH);
  }
  
  public void addMarker(Coordinate coord, Photo photo)
  {
    markerPainter.addMarker(Marker.of(coord), photo);
  }
  
  public MarkerPainter<Photo> markers() { return markerPainter; }
  
  public void centerAndZoomOn(Bounds bounds)
  {
    map.zoomToFit(bounds, 0.3f);
  }
}

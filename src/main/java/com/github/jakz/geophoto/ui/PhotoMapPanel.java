package com.github.jakz.geophoto.ui;

import javax.swing.JPanel;

import com.github.jakz.geophoto.data.Photo;
import com.pixbits.lib.io.xml.gpx.Bounds;
import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.pixbits.lib.ui.map.MapPanel;
import com.pixbits.lib.ui.map.Marker;
import com.pixbits.lib.ui.map.MarkerPainter;

public class PhotoMapPanel extends MapPanel
{
  MarkerPainter<Photo> markerPainter = new MarkerPainter<Photo>();
  
  public PhotoMapPanel()
  {
    super(600,600);
    addPainter(markerPainter);
  }
  
  public void addMarker(Coordinate coord, Photo photo)
  {
    markerPainter.addMarker(Marker.of(coord), photo);
  }
  
  public MarkerPainter<Photo> markers() { return markerPainter; }
  
  public void centerAndZoomOn(Coordinate coord)
  {
    this.zoomToFit(new Bounds(coord), 0.7f);
  }
}

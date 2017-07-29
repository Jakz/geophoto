package com.github.jakz.geophoto.ui;

import javax.swing.JFrame;

import com.github.jakz.geophoto.data.PhotoFolder;
import com.github.jakz.geophoto.ui.gpx.GpxPanel;
import com.github.jakz.geophoto.ui.gpx.TrackSegmentTable;
import com.github.jakz.geophoto.ui.map.MapElementCache;
import com.pixbits.lib.ui.UIUtils;

public class UI
{
  public static PhotoTable photoTable;
  public static GpxPanel gpxPanel;
  public static MapPanel map;
  public static MapElementCache mapCache;
  
  public static void init(PhotoFolder folder)
  {
    photoTable = new PhotoTable(folder);
    
    JFrame frame = UIUtils.buildFrame(photoTable, "Photo Table");
    //frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    map = new MapPanel();
    
    JFrame mapFrame = UIUtils.buildFrame(map, "Map");
    mapFrame.setVisible(true);
    mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    gpxPanel = new GpxPanel();
    
    JFrame gpxFrame = UIUtils.buildFrame(gpxPanel, "Gpx Tracks");
    gpxFrame.setVisible(true);
    gpxFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
  
  public static MultiPhotoView currentPhotoView() 
  { 
    return photoTable;
  }
}

package com.github.jakz.geophoto.ui;

import javax.swing.JFrame;

import com.github.jakz.geophoto.App;
import com.github.jakz.geophoto.data.PhotoFolder;
import com.github.jakz.geophoto.ui.gpx.GpxPanel;
import com.pixbits.lib.ui.UIUtils;
import com.pixbits.lib.ui.WrapperFrame;

public class UI
{
  public static PhotoList photoTable;
  public static PhotoGrid photoGrid;
  
  public static GpxPanel gpxPanel;
  public static PhotoMapPanel map;
  
  
  public static void init(PhotoFolder folder)
  {
    photoTable = new PhotoList(App.mediator, folder);
    
    /*WrapperFrame<?> frame = UIUtils.buildFrame(photoTable, "Photo Table");
    frame.setVisible(true);
    frame.exitOnClose();*/
    
    photoGrid = new PhotoGrid(App.mediator, folder);
    
    WrapperFrame<?> grid = UIUtils.buildFrame(photoGrid, "Photo Grid");
    grid.setVisible(true);
    grid.exitOnClose();
    
    map = new PhotoMapPanel();
    
    WrapperFrame<?> mapFrame = UIUtils.buildFrame(map, "Map");
    mapFrame.setVisible(true);
    mapFrame.setResizable(true);
    mapFrame.exitOnClose();
    
    gpxPanel = new GpxPanel();
    
    WrapperFrame<?> gpxFrame = UIUtils.buildFrame(gpxPanel, "Gpx Tracks");
    //gpxFrame.setVisible(true);
    gpxFrame.exitOnClose();
  }
  
  public static MultiPhotoView currentPhotoView() 
  { 
    return photoTable;
  }
}

package com.github.jakz.geophoto.ui;

import javax.swing.JFrame;

import com.github.jakz.geophoto.App;
import com.github.jakz.geophoto.data.PhotoFolder;
import com.github.jakz.geophoto.ui.gpx.GpxPanel;
import com.pixbits.lib.ui.UIUtils;
import com.pixbits.lib.ui.WrapperFrame;

public class UI
{
  public PhotoList photoTable;
  public PhotoGrid photoGrid;
  
  public GpxPanel gpxPanel;
  public PhotoMapPanel map;
  
  private StatusBar statusBar;
  
  public void init(PhotoFolder folder)
  {
    statusBar = new StatusBar();
    photoTable = new PhotoList(App.mediator, folder);
    photoGrid = new PhotoGrid(App.mediator, folder);
    map = new PhotoMapPanel();
    gpxPanel = new GpxPanel();

    /*WrapperFrame<?> frame = UIUtils.buildFrame(photoTable, "Photo Table");
    frame.setVisible(true);
    frame.exitOnClose();*/
    
    
    WrapperFrame<?> grid = UIUtils.buildFrame(photoGrid, "Photo Grid");
    grid.setVisible(true);
    grid.exitOnClose();
    
    
    WrapperFrame<?> mapFrame = UIUtils.buildFrame(map, "Map");
    mapFrame.setVisible(true);
    mapFrame.setResizable(true);
    mapFrame.exitOnClose();
    
    
    WrapperFrame<?> gpxFrame = UIUtils.buildFrame(gpxPanel, "Gpx Tracks");
    //gpxFrame.setVisible(true);
    gpxFrame.exitOnClose();
  }
  
  public MultiPhotoView currentPhotoView() 
  { 
    return photoTable;
  }
  
  public StatusBar statusBar() { return statusBar; }
}

package com.github.jakz.geophoto.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.github.jakz.geophoto.App;
import com.github.jakz.geophoto.Mediator;
import com.github.jakz.geophoto.data.PhotoEnumeration;
import com.github.jakz.geophoto.data.PhotoFolder;
import com.github.jakz.geophoto.ui.gpx.GpxPanel;
import com.github.jakz.geophoto.ui.tree.TreeViewPanel;
import com.pixbits.lib.ui.UIUtils;
import com.pixbits.lib.ui.WrapperFrame;

public class UI
{
  public PhotoList photoTable;
  public PhotoGrid photoGrid;
  
  public GpxPanel gpxPanel;
  public PhotoMapPanel map;
  
  private TreeViewPanel treeViewPanel;
  private QuickInfoPanel quickInfoPanel;
  private StatusBar statusBar;
  
  public void init(Mediator mediator, PhotoFolder folder)
  {
    quickInfoPanel = new QuickInfoPanel();
    statusBar = new StatusBar(mediator);
    
    photoTable = new PhotoList(App.mediator, folder);
    photoGrid = new PhotoGrid(App.mediator);
    photoGrid.setPhotos(folder);
    map = new PhotoMapPanel();
    gpxPanel = new GpxPanel();
    treeViewPanel = new TreeViewPanel(App.mediator);

    /*WrapperFrame<?> frame = UIUtils.buildFrame(photoTable, "Photo Table");
    frame.setVisible(true);
    frame.exitOnClose();*/
    
    JSplitPane g = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeViewPanel, photoGrid);
    JPanel p = new JPanel();
    g.setDividerLocation(0.25f);
    p.setLayout(new BorderLayout());
    p.add(g);
    
    WrapperFrame<?> grid = UIUtils.buildFrame(p, "Photo Grid");
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
  
  public void showPhotos(PhotoEnumeration photos)
  {
    currentPhotoView().setPhotos(photos);
  }
  
  public MultiPhotoView currentPhotoView() 
  { 
    return photoGrid;
  }
  
  public TreeViewPanel treeView() { return treeViewPanel; }
  public StatusBar statusBar() { return statusBar; }
  public QuickInfoPanel quickInfo() { return quickInfoPanel; }
  
  
  public static Color background = new Color(214,217,223);
  public static Color selectedBackground = new Color(57,105,138);
}

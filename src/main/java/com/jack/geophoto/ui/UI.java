package com.jack.geophoto.ui;

import javax.swing.JFrame;

import com.jack.geophoto.data.PhotoFolder;
import com.pixbits.lib.ui.UIUtils;

public class UI
{
  public static PhotoTable photoTable;
  public static MapPanel map;
  
  public static void init(PhotoFolder folder)
  {
    photoTable = new PhotoTable(folder);
    
    JFrame frame = UIUtils.buildFrame(photoTable, "Photo Table");
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    map = new MapPanel();
    
    JFrame mapFrame = UIUtils.buildFrame(map, "Map");
    mapFrame.setVisible(true);
    mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

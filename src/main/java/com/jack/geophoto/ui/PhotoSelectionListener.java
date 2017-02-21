package com.jack.geophoto.ui;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.event.ListSelectionListener;

import com.jack.geophoto.data.Photo;
import com.pixbits.lib.ui.table.DataSource;
import com.pixbits.lib.ui.table.ManagedListSelectionListener;
import com.pixbits.lib.ui.table.SimpleListSelectionListener;

public class PhotoSelectionListener extends ManagedListSelectionListener<Photo>
{
  PhotoSelectionListener(DataSource<Photo> data)
  {
    super(data);
  }
  
  @Override
  protected void commonActionBefore()
  {
    UI.map.markers().clearAllMarkers();
  }
  
  @Override
  protected void commonActionAfter()
  {
    
  }
  
  @Override
  protected void clearSelection()
  {
    System.out.println("Clear selection");
  }
  
  @Override
  protected void singleSelection(Photo photo)
  {
    System.out.println("Selected: "+photo);
    if (photo.coordinate().isValid())
      UI.map.markers().addOrphanMarker(photo.coordinate());
  }
  
  @Override
  protected void multipleDataSelection(List<Photo> photos)
  {
    String str = photos.stream().map(Object::toString).collect(Collectors.joining(", "));
    System.out.println("Selected: "+str);
    
    photos.forEach(photo -> { 
      if (photo.coordinate().isValid())
        UI.map.markers().addOrphanMarker(photo.coordinate());
    });
  }

}

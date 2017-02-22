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
    UI.map.markers().clearMarkers();
  }
  
  @Override
  protected void commonActionAfter()
  {
    UI.map.fitMarkers(UI.map.markers().markers());
  }
  
  @Override
  protected void clearSelection()
  {

  }
  
  @Override
  protected void singleSelection(Photo photo)
  {
    if (photo.coordinate().isValid())
      UI.map.markers().addMarker(photo, photo.coordinate());
  }
  
  @Override
  protected void multipleDataSelection(List<Photo> photos)
  {
    photos.forEach(photo -> { 
      if (photo.coordinate().isValid())
        UI.map.markers().addMarker(photo, photo.coordinate());
    });
  }

}

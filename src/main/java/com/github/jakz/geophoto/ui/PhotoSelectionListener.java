package com.github.jakz.geophoto.ui;

import java.util.List;
import com.github.jakz.geophoto.data.Photo;
import com.pixbits.lib.ui.table.DataSource;
import com.pixbits.lib.ui.table.ManagedListSelectionListener;

public class PhotoSelectionListener extends ManagedListSelectionListener<Photo>
{
  PhotoSelectionListener(DataSource<Photo> data)
  {
    super(data);
  }
  
  @Override
  protected void commonActionBefore()
  {
    //TODO: UI.map.markers().clearMarkers();
  }
  
  @Override
  protected void commonActionAfter()
  {
  //TODO: UI.map.fitMarkers(UI.map.markers().markers());
  }
  
  @Override
  protected void clearSelection()
  {

  }
  
  @Override
  protected void singleSelection(Photo photo)
  {
    if (photo.coordinate().isValid())
    {
      UI.map.addMarker(photo.coordinate(), photo);
      UI.map.centerAndZoomOn(photo.coordinate());
    }
  }
  
  @Override
  protected void multipleDataSelection(List<Photo> photos)
  {
    photos.forEach(photo -> { 
      if (photo.coordinate().isValid())
        ;//TODO: UI.map.markers().addMarker(photo, photo.coordinate());
    });
  }

}

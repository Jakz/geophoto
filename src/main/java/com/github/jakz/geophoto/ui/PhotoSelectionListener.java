package com.github.jakz.geophoto.ui;

import java.util.List;

import com.github.jakz.geophoto.Mediator;
import com.github.jakz.geophoto.data.Photo;
import com.pixbits.lib.ui.table.DataSource;
import com.pixbits.lib.ui.table.ManagedListSelectionListener;

public class PhotoSelectionListener extends ManagedListSelectionListener<Photo>
{
  private final Mediator mediator;
  
  PhotoSelectionListener(Mediator mediator, DataSource<Photo> data)
  {
    super(data);
    this.mediator = mediator;
  }
  
  @Override
  protected void commonActionBefore()
  {
    mediator.ui().map.markers().clear();
    
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
      PhotoMapPanel map = mediator.ui().map;
      map.addMarker(photo.coordinate(), photo);
      map.centerAndZoomOn(photo.coordinate());
      map.markers().invalidate();
    }
  }
  
  @Override
  protected void multipleDataSelection(List<Photo> photos)
  {
    photos.forEach(photo -> { 
      if (photo.coordinate().isValid())
      {
        mediator.ui().map.addMarker(photo.coordinate(), photo);
      }
    });
    mediator.ui().map.markers().invalidate();
  }

}

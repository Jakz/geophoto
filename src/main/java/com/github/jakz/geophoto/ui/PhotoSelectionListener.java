package com.github.jakz.geophoto.ui;

import java.util.List;

import com.github.jakz.geophoto.Log;
import com.github.jakz.geophoto.Mediator;
import com.github.jakz.geophoto.data.Photo;
import com.pixbits.lib.io.xml.gpx.Bounds;
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
    mediator.ui().map.markers().clear();
    mediator.ui().statusBar().setText("");
  }
  
  @Override
  protected void singleSelection(Photo photo)
  {
    if (photo.coordinate() != null && photo.coordinate().isValid())
    {
      PhotoMapPanel map = mediator.ui().map;
      map.addMarker(photo.coordinate(), photo);
      map.centerAndZoomOn(new Bounds(photo.coordinate()));
      map.markers().invalidate();
    }
    
    mediator.ui().statusBar().setText("Selected 1 photo");
    mediator.ui().quickInfo().updateFor(photo);
  }
  
  @Override
  protected void multipleDataSelection(List<Photo> photos)
  {
    Log.d("ui", "selected %d photos", photos.size());
    Bounds bounds = new Bounds();
    photos.forEach(photo -> 
    { 
      if (photo.coordinate() != null && photo.coordinate().isValid())
      {
        mediator.ui().map.addMarker(photo.coordinate(), photo);
        bounds.updateBound(photo.coordinate());
      }
    });
    
    mediator.ui().map.centerAndZoomOn(bounds);
    mediator.ui().map.markers().invalidate();
    mediator.ui().statusBar().setText("Selected "+photos.size()+" photos");
  }

}

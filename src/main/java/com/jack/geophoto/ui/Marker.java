package com.jack.geophoto.ui;

import java.util.Objects;
import java.util.Optional;

import com.jack.geophoto.data.Coordinate;
import com.jack.geophoto.data.Photo;
import com.jack.geophoto.ui.UI;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapMouseEvent;
import com.teamdev.jxmaps.MouseEvent;

class Marker<T extends MarkerSource> extends com.teamdev.jxmaps.Marker
{
  private final Optional<T> owner;
  private final Coordinate coordinate;
  
  public Marker(Map map, Coordinate coordinate, T owner)
  {
    super(map);
    this.owner = Optional.ofNullable(owner);
    this.coordinate = coordinate;
    setPosition(this.coordinate.toLatLng());
    
    this.setClickable(true);
    this.addEventListener("click", new MapMouseEvent() {
      @Override public void onEvent(MouseEvent event) { onClick(); }
    });
  }
    
  private void onClick()
  {
    owner.ifPresent(o -> {
      if (o.hasAction())
        UI.currentPhotoView().selectPhoto((Photo)o);
    });
  }
  
  public Marker(Map map, Coordinate coordinate)
  {
    this(map, coordinate, null);
  }
    
  @Override
  public int hashCode()
  {
    return Objects.hash(coordinate, owner);
  }
  
  @Override 
  public boolean equals(Object o)
  {
    if ((o instanceof Marker))
    {
      Marker<?> m = (Marker<?>)o;     
      return owner == m.owner && coordinate.equals(m.coordinate);
    }
    else
      return false;
  }
  
  public Coordinate coordinate() { return coordinate; }
}
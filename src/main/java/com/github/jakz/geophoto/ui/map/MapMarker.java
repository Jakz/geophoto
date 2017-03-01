package com.github.jakz.geophoto.ui.map;

import com.github.jakz.geophoto.data.Coordinate;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.Marker;

public class MapMarker extends Marker implements Positionable, MapElement
{
  private final Map map;
  
  private Coordinate coordinate;

  public MapMarker(Map map)
  {
    super(map);
    this.map = map;
  }

  @Override
  public Map map() { return map; }

  @Override
  public Iterable<Coordinate> coordinates() { return null; }
  
  @Override
  public void remove()
  {
    super.remove();
  }

  @Override
  public void hide()
  {
    setVisible(false);
    
  }

  @Override
  public void show()
  {
    setVisible(true);
  }
}

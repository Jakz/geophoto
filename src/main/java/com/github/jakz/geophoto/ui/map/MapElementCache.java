package com.github.jakz.geophoto.ui.map;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.jakz.geophoto.ui.map.builders.GpxTrackLineBuilder;
import com.github.jakz.geophoto.ui.map.builders.MapElementBuilder;

@SuppressWarnings("unchecked")
public class MapElementCache
{
  private final com.teamdev.jxmaps.Map map;
  private final Map<MapElement, Object> mapping;
  private final Map<Object, MapElement> rmapping;
  
  private final Map<Class<?>, MapElementBuilder<?>> builders;
  
  public MapElementCache(com.teamdev.jxmaps.Map map)
  {
    this.map = map;
    this.mapping = new HashMap<>();
    this.rmapping = new HashMap<>();
    
    builders = new HashMap<>();
    builders.put(GpsTrackLine.class, new GpxTrackLineBuilder(map));
  }
  
  private <T extends MapElement> T build(Class<T> type)
  {
    MapElementBuilder<T> builder = (MapElementBuilder<T>) builders.get(type);
    return builder.build();
  }
  
  private void emplace(Object key, MapElement element)
  {
    mapping.put(element, key);
    rmapping.put(key, element);
  }
  
  public <T extends MapElement> T getOrBuild(Class<T> type, Object key)
  {
    MapElement element = rmapping.get(key);
    
    if (element == null)
    {
      element = build(type);
      emplace(key, element);
    }
    
    return (T) element; 
  }
  
  private void forEach(Predicate<MapElement> filter, Consumer<? super MapElement> lambda)
  {
    rmapping.values().stream().filter(filter).forEach(lambda);
  }
  
  public void hideAll()
  {
    rmapping.values().forEach(MapElement::hide);
  }
  
  public void hideAllOf(final Class<?> type)
  {
    forEach(e -> type.isAssignableFrom(e.getClass()), MapElement::hide);
  }
  
  public void hideAllNotOf(Class<?> type)
  {
    forEach(e -> !type.isAssignableFrom(e.getClass()), MapElement::hide);
  }
  

}

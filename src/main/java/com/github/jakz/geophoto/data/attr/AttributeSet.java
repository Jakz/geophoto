package com.github.jakz.geophoto.data.attr;

import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;

import com.github.jakz.geophoto.tools.ExifResult;
import com.pixbits.lib.io.xml.gpx.Coordinate;

public class AttributeSet implements Iterable<Map.Entry<Attr, Object>>
{
  private final EnumMap<Attr, Object> attrs;
  
  public AttributeSet()
  { 
    attrs = new EnumMap<>(Attr.class);   
  }

  public AttributeSet(ExifResult result)
  {
    this();
    load(result);
  }
  
  public void load(ExifResult result)
  {
    for (Attr attr : Attr.values())
      set(attr, attr.parse(result));
  }
    
  public Iterator<Map.Entry<Attr, Object>> iterator() { return attrs.entrySet().iterator(); }
  
  public void set(Attr key, Object value) { attrs.put(key, value); }
  @SuppressWarnings("unchecked") public <T> T get(Attr key) { return (T)attrs.get(key); }
  
  public void coordinate(Coordinate coordinate) { set(Attr.COORDINATE, coordinate); }
  
  public Coordinate coordinate() { return get(Attr.COORDINATE); }
  
  
  public String toString()
  {
    StringBuilder str = new StringBuilder();
    
    for (Map.Entry<Attr,Object> entry : attrs.entrySet())
      str.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");

    
    return str.toString();
  }
}

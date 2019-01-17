package com.github.jakz.geophoto.data.attr;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

import com.github.jakz.geophoto.cache.db.PersistentDatabase;
import com.github.jakz.geophoto.data.tags.ExposureProgram;
import com.github.jakz.geophoto.data.tags.Orientation;
import com.github.jakz.geophoto.tools.Exif;
import com.github.jakz.geophoto.tools.ExifResult;
import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.thebuzzmedia.exiftool.Tag;
import com.thebuzzmedia.exiftool.core.NonConvertedTag;
import com.thebuzzmedia.exiftool.core.StandardTag;

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
  public <T> T get(Attr key) { return (T)attrs.get(key); }
  
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

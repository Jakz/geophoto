package com.github.jakz.geophoto.data;

import java.util.Iterator;
import java.util.List;

import com.pixbits.lib.ui.table.DataSource;

public interface PhotoEnumeration extends DataSource<Photo>
{
  @Override int size();
  @Override Photo get(int index);
  Iterator<Photo> iterator();
  String title();
  
  public static PhotoEnumeration of(List<Photo> photos, String title)
  {
    return new PhotoEnumeration()
    {
      @Override public int size() { return photos.size(); }
      @Override public Photo get(int index) { return photos.get(index); }
      @Override public Iterator<Photo> iterator() { return photos.iterator(); }
      @Override public int indexOf(Photo photo) { return photos.indexOf(photo); }
      @Override public String title() { return title; }
    };
  }
}

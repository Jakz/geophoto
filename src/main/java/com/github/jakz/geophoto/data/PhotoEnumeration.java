package com.github.jakz.geophoto.data;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import com.pixbits.lib.ui.table.DataSource;
import com.pixbits.lib.ui.table.FilterableDataSource;

public interface PhotoEnumeration extends FilterableDataSource<Photo>
{
  @Override int size();
  @Override Photo get(int index);
  Iterator<Photo> iterator();
  String title();
  
  public static PhotoEnumeration of(List<Photo> photos, String title)
  {
    return new PhotoSet(photos)
    {
      @Override public String title() { return title; }
    };
  }
}

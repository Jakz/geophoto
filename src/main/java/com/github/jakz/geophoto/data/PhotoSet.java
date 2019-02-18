package com.github.jakz.geophoto.data;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.pixbits.lib.ui.table.FilterableListDataSource;

public abstract class PhotoSet implements PhotoEnumeration
{
  protected final FilterableListDataSource<Photo> photos;


  public PhotoSet(Collection<Photo> photos)
  {
    this.photos = new FilterableListDataSource<>(photos);
  }

  public Iterable<Photo> photos() { return photos; }

  public void sort() 
  {
    photos.sort(PhotoSorter.ByFileName);
  }
  
  public abstract String title();


  @Override public int size() { return photos.size(); }
  @Override public Photo get(int index) { return photos.get(index); }
  @Override public Iterator<Photo> iterator() { return photos.iterator(); }
  @Override public int indexOf(Photo object) { return photos.indexOf(object); }

  public void forEach(Consumer<? super Photo> consumer) { photos.forEach(consumer); }

  @Override public void clearFilter() { photos.clearFilter(); }

  @Override public void filter(Predicate<? super Photo> predicate) { photos.filter(predicate); }

}
package com.jack.geophoto.data;

import java.util.Iterator;

import com.pixbits.lib.ui.table.TableDataSource;

public interface PhotoEnumeration extends TableDataSource<Photo>
{
  @Override int size();
  @Override Photo get(int index);
  Iterator<Photo> iterator();
}

package com.jack.geophoto.data;

import java.util.Iterator;

import com.pixbits.lib.ui.table.DataSource;

public interface PhotoEnumeration extends DataSource<Photo>
{
  @Override int size();
  @Override Photo get(int index);
  Iterator<Photo> iterator();
}

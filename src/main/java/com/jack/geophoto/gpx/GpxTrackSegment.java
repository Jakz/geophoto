package com.jack.geophoto.gpx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.pixbits.lib.ui.table.DataSource;

public class GpxTrackSegment implements DataSource<GpxWaypoint>
{
  List<GpxWaypoint> points;
  
  GpxTrackSegment()
  {
    points = new ArrayList<>();
  }
  
  public List<GpxWaypoint> points() { return points; }
  @Override public Iterator<GpxWaypoint> iterator() { return points.iterator(); }
  @Override public GpxWaypoint get(int index) { return points.get(index); }
  @Override public int size() { return points.size(); }
  @Override public int indexOf(GpxWaypoint object) { return points.indexOf(object); }
}

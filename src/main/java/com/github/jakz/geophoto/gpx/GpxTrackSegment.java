package com.github.jakz.geophoto.gpx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import com.pixbits.lib.ui.table.DataSource;

public class GpxTrackSegment implements DataSource<GpxWaypoint>
{
  List<GpxWaypoint> points;
  private double[] distanceCache;
  
  GpxTrackSegment()
  {
    points = new ArrayList<>();
  }
  
  public double distanceBetweenPoints(int index)
  {
    if (distanceCache == null)
    {
      distanceCache = new double[points.size()];
      Arrays.fill(distanceCache, Double.NaN);
    }
    
    if (distanceCache[index] == Double.NaN)
      distanceCache[index] = points.get(index).coordinate.distance(points.get(index+1).coordinate);
    
    return distanceCache[index];
  }
  
  public List<GpxWaypoint> points() { return points; }
  @Override public Iterator<GpxWaypoint> iterator() { return points.iterator(); }
  @Override public GpxWaypoint get(int index) { return points.get(index); }
  @Override public int size() { return points.size(); }
  @Override public int indexOf(GpxWaypoint object) { return points.indexOf(object); }
}

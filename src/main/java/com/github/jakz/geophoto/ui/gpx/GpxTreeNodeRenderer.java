package com.github.jakz.geophoto.ui.gpx;

import java.awt.Component;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.github.jakz.geophoto.gpx.Gpx;
import com.github.jakz.geophoto.gpx.GpxTrack;
import com.github.jakz.geophoto.gpx.GpxTrackSegment;
import com.pixbits.lib.util.TimeInterval;

public class GpxTreeNodeRenderer extends DefaultTreeCellRenderer
{
  private final DateTimeFormatter formatter;
  
  GpxTreeNodeRenderer()
  {
    formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
  }
  
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object object, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
  {
    JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, object, sel, expanded, leaf, row, hasFocus);

    
    if (object instanceof GpxFileTreeNode)
    {
      Gpx gpx = ((GpxFileTreeNode)object).getGpx();
      
      String title = gpx.name() != null ? gpx.name() : "Gpx";
      String date = formatter.format(gpx.time());
      int trackCount = gpx.tracks().size();
      
      label.setText(String.format("%s (%d %s) (%s)", title, trackCount, trackCount > 1 ? "tracks" : "track", date));
    }
    else if (object instanceof GpxTrackTreeNode)
    {
      GpxTrack track = ((GpxTrackTreeNode)object).getTrack();
      
      String title = track.name() != null ? track.name() : "Track";
      int segmentCount = track.segments().size();
      
      label.setText(String.format("%s (%d %s)", title, segmentCount, segmentCount > 1 ? "segments" : "segment"));
    }
    else if (object instanceof GpxTrackSegmentTreeNode)
    {
      GpxTrackSegment segment = ((GpxTrackSegmentTreeNode)object).getSegment();
     
      String duration = "";
      
      if (!segment.points().isEmpty())
      {
        Temporal start = segment.points().get(0).time();
        Temporal end = segment.points().get(segment.points().size()-1).time();
        TimeInterval interval = TimeInterval.of(start, end);
        duration = String.format("%02d:%02d:%02d", interval.hours(), interval.minutes(), interval.seconds());
      }

      int waypointCount = segment.points().size();
      
      label.setText(String.format("%s (%d %s)", duration, waypointCount, waypointCount > 1 ? "waypoints" : "waypoint"));
    }
    
    return label;
  }
}

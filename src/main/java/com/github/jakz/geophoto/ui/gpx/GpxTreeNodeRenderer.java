package com.github.jakz.geophoto.ui.gpx;

import java.awt.Color;
import java.awt.Component;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.Temporal;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.pixbits.lib.io.xml.gpx.Gpx;
import com.pixbits.lib.io.xml.gpx.GpxTrack;
import com.pixbits.lib.io.xml.gpx.GpxTrackSegment;
import com.pixbits.lib.ui.color.PleasantColorGenerator;
import com.pixbits.lib.ui.color.SquareIconGenerator;
import com.pixbits.lib.util.TimeInterval;

public class GpxTreeNodeRenderer extends DefaultTreeCellRenderer
{
  private final DateTimeFormatter formatter;
  private final SquareIconGenerator iconGenerator;
  
  GpxTreeNodeRenderer()
  {
    formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.SHORT);
    iconGenerator = new SquareIconGenerator(new PleasantColorGenerator(), 10, 10, 1, Color.black);
  }
  
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object object, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
  {
    String text = "";
    
    setLeafIcon(getDefaultLeafIcon());
    
    if (object instanceof GpxFileTreeNode)
    {
      Gpx gpx = ((GpxFileTreeNode)object).getGpx();
      
      String title = gpx.name() != null ? gpx.name() : "Gpx";
      String date = formatter.format(gpx.time());
      int trackCount = gpx.tracks().size();
      
      text = String.format("%s (%d %s) (%s)", title, trackCount, trackCount > 1 ? "tracks" : "track", date);
    }
    else if (object instanceof GpxTrackTreeNode)
    {
      GpxTrack track = ((GpxTrackTreeNode)object).getTrack();
      
      String title = track.name() != null ? track.name() : "Track";
      int segmentCount = track.segments().size();
      
      text = String.format("%s (%d %s)", title, segmentCount, segmentCount > 1 ? "segments" : "segment");
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

      double length = segment.totalLength();
      int waypointCount = segment.points().size();
      
      text = String.format("%s (%d %s) (%.2fkm)", duration, waypointCount, waypointCount > 1 ? "waypoints" : "waypoint", length);
      setLeafIcon(iconGenerator.generateIcon(segment.color()));
    }
    
    JLabel label = (JLabel)super.getTreeCellRendererComponent(tree, text, sel, expanded, leaf, row, hasFocus);

    return label;
  }
}

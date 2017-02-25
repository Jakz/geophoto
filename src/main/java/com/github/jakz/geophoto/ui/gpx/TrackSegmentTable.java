package com.github.jakz.geophoto.ui.gpx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.time.ZonedDateTime;
import java.time.format.FormatStyle;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.github.jakz.geophoto.gpx.GpxTrackSegment;
import com.github.jakz.geophoto.gpx.GpxWaypoint;
import com.github.jakz.geophoto.ui.renderers.DateTimeRenderer;
import com.github.jakz.geophoto.ui.renderers.DistanceRenderer;
import com.github.jakz.geophoto.ui.renderers.GeographicCoordRenderer;
import com.github.jakz.geophoto.ui.renderers.TimeIntervalRenderer;
import com.pixbits.lib.ui.table.ColumnSpec;
import com.pixbits.lib.ui.table.TableModel;
import com.pixbits.lib.util.TimeInterval;

public class TrackSegmentTable extends JPanel
{
  private GpxTrackSegment segment;
  
  private final TableModel<GpxWaypoint> model;
  
  private final JTable table;
  private final JScrollPane scrollPane;
  
  private final GeographicCoordRenderer geographicCoordRenderer;
  private final DistanceRenderer distanceRenderer;

  
  public TrackSegmentTable()
  {
    this.segment = null;
    table = new JTable();
    scrollPane = new JScrollPane(table);
    model = new TableModel<>(table, scrollPane, segment);
    
    table.setFont(table.getFont().deriveFont(table.getFont().getSize()*0.75f));
    table.getTableHeader().setFont(table.getFont().deriveFont(Font.BOLD));
    
    scrollPane.setPreferredSize(new Dimension(300,400));
    
    geographicCoordRenderer = new GeographicCoordRenderer();
    distanceRenderer = new DistanceRenderer();
    
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    
    ColumnSpec<GpxWaypoint, Integer> idSpec = new ColumnSpec<>("#", Integer.class);
    idSpec.setGetter((i,v) -> i);
    model.addColumn(idSpec);
    
    ColumnSpec<GpxWaypoint, ZonedDateTime> timeSpec = new ColumnSpec<>("Time", ZonedDateTime.class);
    timeSpec.setGetter(w -> w.time());
    timeSpec.setRenderer(new DateTimeRenderer(false, true, FormatStyle.MEDIUM));
    model.addColumn(timeSpec);
    
    ColumnSpec<GpxWaypoint, TimeInterval> lapseSpec = new ColumnSpec<>("Lapse", TimeInterval.class);
    lapseSpec.setGetter((i,w) -> {
      if (i < segment.size() - 1)
        return TimeInterval.of(w.time(), segment.get(i+1).time());
      else
        return null;
    });
    lapseSpec.setRenderer(new TimeIntervalRenderer(""));
    model.addColumn(lapseSpec);
    
    ColumnSpec<GpxWaypoint, Double> latSpec = new ColumnSpec<>("Lat", Double.class, w -> w.coordinate().lat());
    latSpec.setRenderer(geographicCoordRenderer);
    model.addColumn(latSpec);
    ColumnSpec<GpxWaypoint, Double> lngSpec = new ColumnSpec<>("Long", Double.class, w -> w.coordinate().lng());
    lngSpec.setRenderer(geographicCoordRenderer);
    model.addColumn(lngSpec);
    
    ColumnSpec<GpxWaypoint, Double> distanceSpec = new ColumnSpec<>("Distance", Double.class);
    distanceSpec.setGetter((i,w) -> {
      if (i < segment.size() - 1)
        return w.coordinate().distance(segment.get(i+1).coordinate());
      else
        return 0.0;
    });
    distanceSpec.setRenderer(distanceRenderer);
    model.addColumn(distanceSpec);
  }
  
  public void setSegment(GpxTrackSegment segment)
  {
    this.segment = segment;
    model.setData(segment);
  }
}

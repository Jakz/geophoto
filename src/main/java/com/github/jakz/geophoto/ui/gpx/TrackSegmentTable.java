package com.github.jakz.geophoto.ui.gpx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.time.ZonedDateTime;
import java.time.format.FormatStyle;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.github.jakz.geophoto.ui.renderers.GeographicCoordRenderer;
import com.github.jakz.geophoto.ui.renderers.SpeedRenderer;
import com.pixbits.lib.io.xml.gpx.GpxTrackSegment;
import com.pixbits.lib.io.xml.gpx.GpxWaypoint;
import com.pixbits.lib.ui.table.ColumnSpec;
import com.pixbits.lib.ui.table.DataSource;
import com.pixbits.lib.ui.table.TableModel;
import com.pixbits.lib.ui.table.renderers.DateTimeRenderer;
import com.pixbits.lib.ui.table.renderers.DistanceRenderer;
import com.pixbits.lib.ui.table.renderers.TimeIntervalRenderer;
import com.pixbits.lib.util.TimeInterval;

public class TrackSegmentTable extends JPanel
{
  private GpxTrackSegment segment;
  
  private final TableModel<GpxWaypoint> model;
  
  private final JTable table;
  private final JScrollPane scrollPane;
  
  private final GeographicCoordRenderer geographicCoordRenderer;
  private final DistanceRenderer distanceRenderer;
  private final SpeedRenderer speedRenderer;

  
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
    speedRenderer = new SpeedRenderer();
    
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
    
    ColumnSpec<GpxWaypoint, Double> speedSpec = new ColumnSpec<>("Speed", Double.class);
    speedSpec.setGetter((i,w) -> {
      if (i < segment.size() - 1)
        return w.coordinate().distance(segment.get(i+1).coordinate()) / TimeInterval.of(w.time(), segment.get(i+1).time()).seconds();
      else
        return 0.0;
    });
    speedSpec.setRenderer(speedRenderer);
    model.addColumn(speedSpec);
  }
  
  public void setSegment(GpxTrackSegment segment)
  {
    this.segment = segment;
    model.setData(segment);
  }
  
  public void clearSegment()
  {
    this.segment = null;
    model.setData(DataSource.of(Collections.emptyList()));
  }
}

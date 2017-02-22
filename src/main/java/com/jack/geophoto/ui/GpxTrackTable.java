package com.jack.geophoto.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.jack.geophoto.gpx.GpxTrackSegment;
import com.jack.geophoto.gpx.GpxWaypoint;
import com.pixbits.lib.ui.table.ColumnSpec;
import com.pixbits.lib.ui.table.TableModel;

public class GpxTrackTable extends JPanel
{
  private GpxTrackSegment segment;
  
  private final TableModel<GpxWaypoint> model;
  
  private final JTable table;
  private final JScrollPane scrollPane;
  
  public GpxTrackTable()
  {
    this.segment = null;
    table = new JTable();
    scrollPane = new JScrollPane(table);
    model = new TableModel<>(table, scrollPane, segment);
    
    scrollPane.setPreferredSize(new Dimension(300,400));
    
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    
    ColumnSpec<GpxWaypoint, Integer> idSpec = new ColumnSpec<>("#", Integer.class);
    idSpec.setGetter((i,v) -> i);
    
    model.addColumn(idSpec);
  }
  
  void setSegment(GpxTrackSegment segment)
  {
    this.segment = segment;
    model.setData(segment);
  }
}

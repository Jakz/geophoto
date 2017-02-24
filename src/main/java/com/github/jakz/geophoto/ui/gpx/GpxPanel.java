package com.github.jakz.geophoto.ui.gpx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.github.jakz.geophoto.gpx.GpxTrack;
import com.github.jakz.geophoto.gpx.GpxTrackSegment;
import com.github.jakz.geophoto.ui.UI;
import com.github.jakz.geophoto.ui.map.GpsTrackLine;

public class GpxPanel extends JPanel implements TreeSelectionListener
{
  private final JSplitPane pane;
  private final TrackSegmentTable segmentTable;
  private final JTree trackTree;
  private final DefaultTreeModel model;
  
  private GpxRootTreeNode root;
  
  public GpxPanel()
  {   
    segmentTable = new TrackSegmentTable();
    trackTree = new JTree();
    trackTree.setRootVisible(false);
    trackTree.addTreeSelectionListener(this);
    
    root = new GpxRootTreeNode();
    model = new DefaultTreeModel(root);
    trackTree.setModel(model);
    
    JScrollPane treePane = new JScrollPane(trackTree);

    pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treePane, segmentTable);

    setLayout(new BorderLayout());
    add(pane, BorderLayout.CENTER);
    
    setPreferredSize(new Dimension(300,600));
  }
  
  public void setTracks(List<GpxTrack> tracks)
  {
    root.setTracks(tracks);
    model.reload(root);
    pane.setDividerLocation(0.3f);
  }
  
  public void rebuildModel()
  {
    
  }

  @Override
  public void valueChanged(TreeSelectionEvent e)
  {
    Object object = e.getPath().getLastPathComponent();
    
    if (object instanceof GpxTrackSegmentTreeNode)
    {
      GpxTrackSegmentTreeNode trackNode = (GpxTrackSegmentTreeNode)object;
      GpxTrackSegment segment = trackNode.getSegment();
      
      
      segmentTable.setSegment(segment);
      GpsTrackLine line = new GpsTrackLine(segment, UI.map.map);
      line.centerAndFit();
    }
    
  }
}

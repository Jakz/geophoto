package com.github.jakz.geophoto.ui.gpx;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.github.jakz.geophoto.ui.UI;
import com.github.jakz.geophoto.ui.map.GpsTrackLine;
import com.pixbits.lib.io.xml.gpx.Gpx;
import com.pixbits.lib.io.xml.gpx.GpxTrackSegment;

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
    trackTree.setCellRenderer(new GpxTreeNodeRenderer());
    
    root = new GpxRootTreeNode();
    model = new DefaultTreeModel(root);
    trackTree.setModel(model);
    
    JScrollPane treePane = new JScrollPane(trackTree);

    pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, treePane, segmentTable);

    setLayout(new BorderLayout());
    add(pane, BorderLayout.CENTER);
    
    setPreferredSize(new Dimension(500,800));
  }
  
  public void setTracks(List<Gpx> files)
  {
    root.setFiles(files);
    model.reload(root);
    pane.setDividerLocation(0.3f);
  }
  
  public void rebuildModel()
  {
    
  }

  @Override
  public void valueChanged(TreeSelectionEvent e)
  {
    TreePath[] paths = trackTree.getSelectionPaths();
    UI.mapCache.hideAll();

    Arrays.stream(paths).forEach(path -> {
      Object object = path.getLastPathComponent();
      
      if (object instanceof GpxTrackSegmentTreeNode)
      {
        GpxTrackSegmentTreeNode trackNode = (GpxTrackSegmentTreeNode)object;
        GpxTrackSegment segment = trackNode.getSegment();

        if (paths.length == 1)
          segmentTable.setSegment(segment);
        else
          segmentTable.clearSegment();
        
        GpsTrackLine line = UI.mapCache.getOrBuild(GpsTrackLine.class, segment);
        line.setSegment(segment);
        line.centerAndFit();
        line.show();
      }
    });
  }
}

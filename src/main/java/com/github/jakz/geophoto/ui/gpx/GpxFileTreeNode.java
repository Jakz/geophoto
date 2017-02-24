package com.github.jakz.geophoto.ui.gpx;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.tree.TreeNode;

import com.github.jakz.geophoto.gpx.Gpx;
import com.github.jakz.geophoto.gpx.GpxTrack;

public class GpxFileTreeNode implements TreeNode
{
  private final GpxRootTreeNode parent;
  private final Gpx gpx;
  private final List<GpxTrackTreeNode> nodes;
  
  GpxFileTreeNode(GpxRootTreeNode parent, Gpx gpx)
  {
    this.parent = parent;
    this.gpx = gpx;
    
    nodes = gpx.tracks().stream()
      .map(t -> new GpxTrackTreeNode(this, t))
      .collect(Collectors.toList());
  }
  
  public Gpx getGpx() { return gpx; }

  @Override
  public TreeNode getChildAt(int index)
  {
    return nodes.get(index);
  }
  
  @Override
  public int getChildCount()
  {
    return nodes.size();
  }
  @Override
  public TreeNode getParent()
  {
    return parent;
  }
  
  @Override
  public int getIndex(TreeNode node)
  {
    return nodes.indexOf(node);
  }
  
  @Override
  public boolean getAllowsChildren()
  {
    return false;
  }
  
  @Override
  public boolean isLeaf()
  {
    return nodes.isEmpty();
  }
  
  @Override
  public Enumeration<?> children()
  {
    return Collections.enumeration(nodes);
  }
  
  @Override
  public boolean equals(Object object)
  {
    return (object instanceof GpxFileTreeNode) && ((GpxFileTreeNode)object).gpx.equals(gpx);
  }
  
  @Override
  public int hashCode()
  {
    return gpx.hashCode();
  }
}

package com.github.jakz.geophoto.ui.gpx;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.tree.TreeNode;

import com.pixbits.lib.io.xml.gpx.GpxTrack;

public class GpxTrackTreeNode implements TreeNode
{
  private final GpxFileTreeNode root;
  private final GpxTrack track;
  private List<GpxTrackSegmentTreeNode> nodes;
  
  GpxTrackTreeNode(GpxFileTreeNode root, GpxTrack track)
  {
    this.root = root;
    this.track = track;
    nodes = track.segments().stream()
      .map(n -> new GpxTrackSegmentTreeNode(this, n))
      .collect(Collectors.toList());
  }
  
  public GpxTrack getTrack() { return track; }
  
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
    return root;
  }

  @Override
  public int getIndex(TreeNode node)
  {
    return nodes.indexOf(node);
  }

  @Override
  public boolean getAllowsChildren()
  {
    return true;
  }

  @Override
  public boolean isLeaf()
  {
    return nodes.isEmpty();
  }

  @Override
  public Enumeration<? extends TreeNode> children()
  {
    return Collections.enumeration(nodes);
  }
  
  @Override
  public boolean equals(Object object)
  {
    return (object instanceof GpxTrackTreeNode) && ((GpxTrackTreeNode)object).track.equals(track);
  }
  
  @Override
  public int hashCode()
  {
    return track.hashCode();
  }
}

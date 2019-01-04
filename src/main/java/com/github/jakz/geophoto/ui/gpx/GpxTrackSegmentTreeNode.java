package com.github.jakz.geophoto.ui.gpx;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import com.pixbits.lib.io.xml.gpx.GpxTrackSegment;

public class GpxTrackSegmentTreeNode implements TreeNode
{
  private final GpxTrackTreeNode parent;
  private final GpxTrackSegment segment;
  
  GpxTrackSegmentTreeNode(GpxTrackTreeNode parent, GpxTrackSegment segment)
  {
    this.parent = parent;
    this.segment = segment;
  }
  
  GpxTrackSegment getSegment() { return segment; }

  @Override
  public TreeNode getChildAt(int childIndex) { return null; }

  @Override
  public int getChildCount() { return 0; }

  @Override
  public TreeNode getParent() { return parent; }

  @Override
  public int getIndex(TreeNode node) { return 0; }

  @Override
  public boolean getAllowsChildren() { return false; }

  @Override
  public boolean isLeaf() { return true; }

  @Override
  public Enumeration<? extends TreeNode> children() { return null; }
  
  @Override
  public boolean equals(Object object)
  {
    return (object instanceof GpxTrackSegmentTreeNode) && ((GpxTrackSegmentTreeNode)object).segment.equals(segment);
  }
  
  @Override
  public int hashCode()
  {
    return segment.hashCode();
  }
}

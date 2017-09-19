package com.github.jakz.geophoto.ui.gpx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.tree.TreeNode;

import com.pixbits.lib.io.xml.gpx.Gpx;

public class GpxRootTreeNode implements TreeNode
{
  private List<GpxFileTreeNode> tracks;
  
  GpxRootTreeNode()
  {
    tracks = new ArrayList<>();
  }
  
  GpxRootTreeNode(Gpx... files)
  {
    this(Arrays.asList(files));
  }
  
  GpxRootTreeNode(List<Gpx> files)
  {
    setFiles(files);  
  }
  
  GpxRootTreeNode(GpxFileTreeNode... nodes)
  {
    tracks = new ArrayList<>(Arrays.asList(nodes));
  }
  
  public void setFiles(List<Gpx> tracks)
  {
    this.tracks = tracks.stream()
      .map(t -> new GpxFileTreeNode(this, t))
      .collect(Collectors.toList());  
  }

  @Override
  public TreeNode getChildAt(int index)
  {
    return tracks.get(index);
  }

  @Override
  public int getChildCount()
  {
    return tracks.size();
  }

  @Override
  public TreeNode getParent()
  {
    return null;
  }

  @Override
  public int getIndex(TreeNode node)
  {
    return tracks.indexOf(node);
  }

  @Override
  public boolean getAllowsChildren()
  {
    return true;
  }

  @Override
  public boolean isLeaf()
  {
    return tracks.isEmpty();
  }

  @Override
  public Enumeration<?> children()
  {
    return Collections.enumeration(tracks);
  }
}

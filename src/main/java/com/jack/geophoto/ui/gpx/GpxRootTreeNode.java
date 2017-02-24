package com.jack.geophoto.ui.gpx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.tree.TreeNode;

import com.jack.geophoto.gpx.GpxTrack;

public class GpxRootTreeNode implements TreeNode
{
  private List<GpxTrackTreeNode> tracks;
  
  GpxRootTreeNode()
  {
    tracks = new ArrayList<>();
  }
  
  GpxRootTreeNode(GpxTrack... tracks)
  {
    this(Arrays.asList(tracks));
  }
  
  GpxRootTreeNode(List<GpxTrack> tracks)
  {
    setTracks(tracks);  
  }
  
  GpxRootTreeNode(GpxTrackTreeNode... nodes)
  {
    tracks = new ArrayList<>(Arrays.asList(nodes));
  }
  
  public void setTracks(List<GpxTrack> tracks)
  {
    this.tracks = tracks.stream()
      .map(t -> new GpxTrackTreeNode(this, t))
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

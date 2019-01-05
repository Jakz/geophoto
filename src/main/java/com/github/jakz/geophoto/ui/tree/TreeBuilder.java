package com.github.jakz.geophoto.ui.tree;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.github.jakz.geophoto.data.PhotoEnumeration;

public class TreeBuilder
{
  public static PhotoTreeNode ofFlatList(Collection<PhotoEnumeration> nodes)
  {
    PhotoTreeNode root = new PhotoTreeNode(null, null, null);
    root.setChildren(nodes.stream().map(e -> new PhotoTreeNode(root, e, null)).collect(Collectors.toList()));
    return root;
  }
}

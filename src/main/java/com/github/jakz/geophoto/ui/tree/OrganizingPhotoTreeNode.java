package com.github.jakz.geophoto.ui.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.swing.tree.TreeNode;

import com.github.jakz.geophoto.data.MutablePhotoEnumeration;
import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.ui.tree.PhotoEnumerationNode.Mutable;

public class OrganizingPhotoTreeNode<K> extends PhotoEnumerationNode.Mutable
{
  Function<Photo, K> keyForPhoto;
  Function<K, MutablePhotoEnumeration> factory;
  
  Map<K, Mutable> mapping;
  
  
  public OrganizingPhotoTreeNode(TreeNode parent, MutablePhotoEnumeration content, List<Mutable> children)
  {
    super(parent, content, children);
    
    mapping = new HashMap<>();
  }
  
  
  @Override
  public void add(Photo photo)
  {
    K key = keyForPhoto.apply(photo);
    
    Mutable node = mapping.computeIfAbsent(key, k -> new Mutable(this, factory.apply(k), new ArrayList<>()));
    node.add(photo);
  }

}

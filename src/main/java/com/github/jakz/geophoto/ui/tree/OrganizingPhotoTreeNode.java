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
  private final Function<Photo, K> keyForPhoto;
  private final Function<K, MutablePhotoEnumeration> factory;
  
  private final Map<K, Mutable> mapping;
  
  
  public OrganizingPhotoTreeNode(TreeNode parent, Function<Photo, K> keyComputer, Function<K, MutablePhotoEnumeration> factory)
  {
    super(parent, null, null);
    
    this.keyForPhoto = keyComputer;
    this.factory = factory;
    
    mapping = new HashMap<>();
  }
  
  
  @Override
  public void add(Photo photo)
  {
    boolean needsResort = false;
    
    K key = keyForPhoto.apply(photo);
    
    if (key == null)
    {
      if (content == null)
        content = factory.apply(key);
      
      content.add(photo);
    }
    
    Mutable node = mapping.get(key);
    
    if (node == null)
    {
      node = new Mutable(this, factory.apply(key), new ArrayList<>());
      needsResort = true;
    }
    
    node.add(photo);
  }
}

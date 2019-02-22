package com.github.jakz.geophoto.ui.tree;

import java.util.List;

import javax.swing.tree.TreeNode;

import com.github.jakz.geophoto.data.MutablePhotoEnumeration;
import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.PhotoEnumeration;

public class PhotoEnumerationNode extends PhotoTreeNode<PhotoEnumerationNode>
{
  protected PhotoEnumeration content;

  public PhotoEnumerationNode(TreeNode parent, PhotoEnumeration content, List<PhotoEnumerationNode> children)
  {
    super(parent, children);
    this.content = content;
  }
   
  @Override public String title()
  { 
    if (content != null)
      return String.format("%s (%d)", content.title(), content.size());
    else
      return "Unnamed";
  }
  
  boolean hasContent() { return true; }
  @Override public PhotoEnumeration content() { return content; } 
  
  public static class Mutable extends PhotoTreeNode<Mutable>
  {
    public Mutable(TreeNode parent, MutablePhotoEnumeration content, List<Mutable> children)
    {
      super(parent, children);
      this.content = content;
    }
    
    protected MutablePhotoEnumeration content;

    @Override
    String title()
    {
      if (content != null)
        return String.format("%s (%d)", content.title(), content.size());
      else
        return "Unnamed";
    }
    
    boolean hasContent() { return true; }
    @Override public MutablePhotoEnumeration content() { return content; } 
    
    public void add(Photo photo) { content.add(photo); }
  }
}

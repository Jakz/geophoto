package com.github.jakz.geophoto.ui.tree;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.github.jakz.geophoto.data.PhotoEnumeration;
import com.github.jakz.geophoto.data.PhotoFolder;

public class PhotoTreeNode implements TreeNode
{
  private final TreeNode parent;
  private List<PhotoTreeNode> children;
  private PhotoEnumeration content;
 
  public PhotoTreeNode(TreeNode parent, PhotoEnumeration content, List<PhotoTreeNode> children)
  {
    this.children = children;
    this.parent = parent;
    this.content = content;
  }
  
  public PhotoEnumeration content() { return content; }
  public void setChildren(List<PhotoTreeNode> children) { this.children = children; }
  
  @Override public TreeNode getChildAt(int i) { return children.get(i); }
  @Override public int getChildCount() { return children.size(); }

  @Override public TreeNode getParent() { return parent; }
  @Override public int getIndex(TreeNode node) { return children.indexOf(node); }

  @Override public boolean getAllowsChildren() { return true; }
  @Override public boolean isLeaf() { return children == null || children.isEmpty(); }
  @Override public Enumeration<? extends TreeNode> children() { return Collections.enumeration(children); }
  
  
  
  
  public static PhotoTreeNode empty() { return new PhotoTreeNode(null, null, null); }
}

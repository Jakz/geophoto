package com.github.jakz.geophoto.ui.tree;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import com.github.jakz.geophoto.data.PhotoEnumeration;

public abstract class PhotoTreeNode<T extends PhotoTreeNode<T>> implements TreeNode
{
  private final TreeNode parent;
  private List<T> children;
 
  public PhotoTreeNode(TreeNode parent, List<T> children)
  {
    this.children = children;
    this.parent = parent;
  }

  boolean hasContent() { return false; }
  PhotoEnumeration content() { return null; }
  abstract String title();
  
  public void setChildren(List<T> children) { this.children = children; }
  
  @Override public TreeNode getChildAt(int i) { return children.get(i); }
  @Override public int getChildCount() { return children.size(); }

  @Override public TreeNode getParent() { return parent; }
  @Override public int getIndex(TreeNode node) { return children.indexOf(node); }

  @Override public boolean getAllowsChildren() { return true; }
  @Override public boolean isLeaf() { return children == null || children.isEmpty(); }
  @Override public Enumeration<? extends TreeNode> children() { return Collections.enumeration(children); }

  public static PhotoTreeNode<PhotoEnumerationNode> empty() { return new PhotoEnumerationNode(null, null, null); }
}

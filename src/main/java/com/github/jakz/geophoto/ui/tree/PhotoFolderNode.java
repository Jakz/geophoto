package com.github.jakz.geophoto.ui.tree;

import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import com.github.jakz.geophoto.data.PhotoFolder;

public class PhotoFolderNode implements TreeNode
{
  PhotoFolder folder;
  
  public PhotoFolderNode(PhotoFolder folder)
  {
    this.folder = folder;
  }
  
  @Override
  public TreeNode getChildAt(int childIndex) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getChildCount() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public TreeNode getParent() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getIndex(TreeNode node) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override public boolean getAllowsChildren() { return true; }

  @Override
  public boolean isLeaf() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Enumeration<? extends TreeNode> children() {
    // TODO Auto-generated method stub
    return null;
  }

}

package com.github.jakz.geophoto.ui.tree;

public class PhotoTree
{
  private PhotoTreeNode<?> root;
  
  public PhotoTree(PhotoTreeNode<?> root)
  {
    this.root = root;
  }
  
  public PhotoTreeNode<?> root() { return root; }
}
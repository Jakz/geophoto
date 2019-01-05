package com.github.jakz.geophoto.ui.tree;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.github.jakz.geophoto.Mediator;

public class TreeViewPanel extends JPanel
{
  private final Mediator mediator;
  
  private final JTree tree;
  private final TreeModel model;
  
  public TreeViewPanel(Mediator mediator)
  {
    this.mediator = mediator;
    
    tree = new JTree();
    model = new DefaultTreeModel(PhotoTreeNode.empty());
    tree.setModel(model);
    tree.setRootVisible(false);
    
    setLayout(new BorderLayout());
    add(tree, BorderLayout.CENTER);
  }
}

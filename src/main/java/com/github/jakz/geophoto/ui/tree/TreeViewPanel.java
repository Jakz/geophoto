package com.github.jakz.geophoto.ui.tree;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import com.github.jakz.geophoto.Mediator;

public class TreeViewPanel extends JPanel
{
  private final Mediator mediator;
  
  private final JTree tree;
  private final DefaultTreeModel model;
  
  public TreeViewPanel(Mediator mediator)
  {
    this.mediator = mediator;
    
    tree = new JTree();
    model = new DefaultTreeModel(PhotoTreeNode.empty());
    tree.setModel(model);
    tree.setRootVisible(false);
    tree.setCellRenderer(new TreeNodeRenderer());
    
    tree.addTreeSelectionListener(e -> {
      PhotoTreeNode node = (PhotoTreeNode)tree.getLastSelectedPathComponent();
      nodeSelected(node);
    });
    
    
    JScrollPane scrollPane = new JScrollPane(tree);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
  }
  
  public void setRoot(PhotoTreeNode root)
  {
    model.setRoot(root);
  }
  
  private void nodeSelected(PhotoTreeNode node)
  {
    
  }
}

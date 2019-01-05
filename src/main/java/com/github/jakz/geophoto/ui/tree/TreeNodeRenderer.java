package com.github.jakz.geophoto.ui.tree;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

public class TreeNodeRenderer extends DefaultTreeCellRenderer
{
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
  {
    JLabel c = (JLabel)super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    
    PhotoTreeNode node = (PhotoTreeNode)value;
    
    if (node.content() != null)
      c.setText(String.format("%s (%d)", node.content().title(), node.content().size()));
    
    return c;
  }
}

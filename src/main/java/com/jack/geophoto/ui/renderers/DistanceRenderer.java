package com.jack.geophoto.ui.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DistanceRenderer extends DefaultTableCellRenderer
{
  public DistanceRenderer()
  {
    
  }
  
  public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column)
  {
    JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
    double value = (double)object; 
    
    if (Double.isNaN(value))
      label.setText("");
    else if (value < 1)
      label.setText((int)(value*1000) + "m");
    else
      label.setText(String.format("%.2fkm", value));
    
    return label;
  }
}

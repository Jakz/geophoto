package com.jack.geophoto.ui.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class GeographicCoordRenderer extends DefaultTableCellRenderer
{
  public GeographicCoordRenderer()
  {
    
  }
  
  public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column)
  {
    JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
    double value = (double)object; 
    
    if (Double.isNaN(value))
      label.setText("");
    else
      label.setText(String.format("%2.8f", value));
    
    return label;
  }
}

package com.github.jakz.geophoto.ui.renderers;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class SpeedRenderer extends DefaultTableCellRenderer
{
  public SpeedRenderer()
  {
    
  }
  
  public Component getTableCellRendererComponent(JTable table, Object object, boolean isSelected, boolean hasFocus, int row, int column)
  {
    JLabel label = (JLabel) super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
    double value = (double)object; 
    
    if (Double.isNaN(value))
      label.setText("");
    else
    {
      double kmPerHour = value*60*60;
      
      if (kmPerHour >= 1)
        label.setText((int)kmPerHour + "km/h");
      else
        label.setText((int)(value*1000) + "m/s");
    }

    return label;
  }
}

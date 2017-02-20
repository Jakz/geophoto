package com.jack.geophoto.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.jack.geophoto.data.Photo;
import com.jack.geophoto.data.PhotoEnumeration;
import com.pixbits.lib.ui.table.ColumnSpec;
import com.pixbits.lib.ui.table.TableModel;

public class PhotoTable extends JPanel
{
  private final TableModel<Photo> model;
  
  private final JTable table;
  private final JScrollPane scrollPane;
  
  public PhotoTable(PhotoEnumeration photos)
  {
    table = new JTable();
    model = new TableModel<>(table, photos);
    
    scrollPane = new JScrollPane(table);
    scrollPane.setPreferredSize(new Dimension(400,800));
    
    table.setRowHeight(60);
        
    ColumnSpec<Photo, ImageIcon> thumbnailColumn = new ColumnSpec<>(
       "",
       ImageIcon.class,
       p -> { 
         BufferedImage i = p.thumbnails().tiny().image();
         return i != null ? new ImageIcon(i) : null;
       },
       null
    );
    
    TableCellRenderer thumbnailRenderer = new DefaultTableCellRenderer()
    {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        JLabel label = (JLabel)super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (value != null)
          label.setIcon((ImageIcon)value);
        
        return label;
      }
    };
    
    thumbnailColumn.setRenderer(thumbnailRenderer);
    
    model.addColumn(thumbnailColumn);
    
    model.addColumn(new ColumnSpec<Photo, String>(
        "Name",
        String.class,
        p -> p.path().getFileName().toString(),
        null
      ));
        
    
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
  }
  
  public void refreshData()
  {
    model.fireTableDataChanged();
  }
}

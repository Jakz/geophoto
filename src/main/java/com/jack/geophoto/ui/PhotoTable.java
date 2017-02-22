package com.jack.geophoto.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.jack.geophoto.cache.Thumbnail;
import com.jack.geophoto.cache.ThumbnailSize;
import com.jack.geophoto.data.Coordinate;
import com.jack.geophoto.data.Photo;
import com.jack.geophoto.data.PhotoEnumeration;
import com.jack.geophoto.tools.ExifResult;
import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.ui.table.ColumnSpec;
import com.pixbits.lib.ui.table.TableModel;

public class PhotoTable extends JPanel implements MultiPhotoView
{
  PhotoEnumeration photos;
  
  private final TableModel<Photo> model;
  
  private final JTable table;
  private final JScrollPane scrollPane;
  
  private class RefreshDataCallback<T> implements BiConsumer<Photo, T>
  {
    @Override
    public void accept(Photo t, T u)
    {
      SwingUtilities.invokeLater(() -> PhotoTable.this.refreshData());
    }  
  }
  
  private final RefreshDataCallback<Thumbnail> thumbnailLoadedCallback = new RefreshDataCallback<>();
  //private final RefreshDataCallback<ExifResult> exifDataLoaded = new RefreshDataCallback<>();
  
  public PhotoTable(PhotoEnumeration photos)
  {
    this.photos = photos;
    table = new JTable();
    scrollPane = new JScrollPane(table);
    model = new TableModel<>(table, scrollPane, photos);
    
    scrollPane.setPreferredSize(new Dimension(400,800));
    
    table.setRowHeight(90);
    table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    table.getSelectionModel().addListSelectionListener(new PhotoSelectionListener(photos));
          
    /* thumbnail */
    ColumnSpec<Photo, ImageIcon> thumbnailColumn = new ColumnSpec<>(
       "",
       ImageIcon.class,
       StreamException.rethrowFunction(p -> { 
         Thumbnail thumbnail = p.thumbnails().asyncGet(ThumbnailSize.TINY, thumbnailLoadedCallback);
         return thumbnail != null ? new ImageIcon(thumbnail.image()) : null;
       }),
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
        else label.setIcon(null);
        
        return label;
      }
    };
    
    thumbnailColumn.setRenderer(thumbnailRenderer);
    
    model.addColumn(thumbnailColumn);
    
    model.addColumn(new ColumnSpec<Photo, String>(
        "Filename",
        String.class,
        p -> p.path().getFileName().toString(),
        null
      ));
        
    /* gps coordinate */
    TableCellRenderer coordinateRenderer = new DefaultTableCellRenderer()
    {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
      {
        JLabel label = (JLabel)super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        if (value != null)
        {
          Coordinate coordinate = (Coordinate)value;
          label.setText(coordinate.isValid() ? String.format("%2.4f, %2.4f", coordinate.lat(), coordinate.lng()) : "UNKNOWN");
        }
        else
          label.setText("");
        
        return label;
      }
    };
    
    ColumnSpec<Photo, Coordinate> coordinateColumn = new ColumnSpec<>(
        "",
        Coordinate.class,
        StreamException.rethrowFunction(p -> { 
          Coordinate coord = p.coordinate();
          return coord;
        }),
        null
    );
    
    coordinateColumn.setRenderer(coordinateRenderer);
    
    model.addColumn(coordinateColumn);
    
    
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
  }
  
  public void refreshData()
  {
    table.repaint();
    //model.fireTableDataChanged();
  }

  @Override
  public void selectPhoto(Photo photo)
  {
    int index = photos.indexOf(photo); 
    table.setRowSelectionInterval(index, index);
  }
}

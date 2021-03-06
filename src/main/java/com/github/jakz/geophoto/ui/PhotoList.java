package com.github.jakz.geophoto.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import com.github.jakz.geophoto.Mediator;
import com.github.jakz.geophoto.cache.Thumbnail;
import com.github.jakz.geophoto.cache.ThumbnailSet;
import com.github.jakz.geophoto.cache.ThumbSize;
import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.PhotoEnumeration;
import com.github.jakz.geophoto.data.geocode.Country;
import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.functional.TriConsumer;
import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.pixbits.lib.ui.table.ColumnSpec;
import com.pixbits.lib.ui.table.TableModel;

public class PhotoList extends JPanel implements MultiPhotoView
{
  PhotoEnumeration photos;
  
  private final Mediator mediator;
  
  private final TableModel<Photo> model;
  
  private final JTable table;
  private final JScrollPane scrollPane;
  
  private class RefreshDataCallback<T> implements TriConsumer<Photo, T, Boolean>
  {
    @Override
    public void accept(Photo t, T u, Boolean isNew)
    {
      SwingUtilities.invokeLater(() -> PhotoList.this.refreshData());
    }  
  }
  
  private final RefreshDataCallback<Thumbnail> thumbnailLoadedCallback = new RefreshDataCallback<>();
  //private final RefreshDataCallback<ExifResult> exifDataLoaded = new RefreshDataCallback<>();
  
  public PhotoList(Mediator mediator, PhotoEnumeration photos)
  {
    this.mediator = mediator;
    
    this.photos = photos;
    table = new JTable();
    scrollPane = new JScrollPane(table);
    model = new TableModel<>(table, scrollPane, photos);
    
    scrollPane.setPreferredSize(new Dimension(400,800));
    //scrollPane.getVerticalScrollBar().setUnitIncrement(1);
    //scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
    
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    
    table.setRowHeight(90);
    table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    table.getSelectionModel().addListSelectionListener(new PhotoSelectionListener(mediator, photos));
          
    /* thumbnail */
    ColumnSpec<Photo, ImageIcon> thumbnailColumn = new ColumnSpec<>(
       "",
       ImageIcon.class,
       StreamException.rethrowFunction(p -> { 
         Thumbnail thumbnail = p.thumbnails().asyncGet(mediator, ThumbSize.TINY, thumbnailLoadedCallback).first;
         return thumbnail != null ? new ImageIcon(thumbnail.image()) : null;
       })
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
        p -> p.path().getFileName().toString()
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
        })
    );
    
    coordinateColumn.setRenderer(coordinateRenderer);
    
    model.addColumn(coordinateColumn);
    
    ColumnSpec<Photo, Country> countryColumn = new ColumnSpec<>(
      "",
      Country.class,
      p -> p.geocode() != null ? p.geocode().country() : Country.UNKNOWN
    );
    
    model.addColumn(countryColumn);
    
    ColumnSpec<Photo, String> cityColumn = new ColumnSpec<>(
        "",
        String.class,
        p -> p.geocode() != null ? p.geocode().city() : null
      );
      
      model.addColumn(cityColumn);  
  }
  
  @Override
  public void setPhotos(PhotoEnumeration photos)
  {
    model.setData(photos);
    table.clearSelection();
    refreshData();
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

  @Override
  public void filter(Predicate<Photo> filter)
  {
    // TODO Auto-generated method stub
  }
}

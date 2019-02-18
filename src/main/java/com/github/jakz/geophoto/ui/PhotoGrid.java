package com.github.jakz.geophoto.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Predicate;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.im4java.core.IM4JavaException;

import com.github.jakz.geophoto.Mediator;
import com.github.jakz.geophoto.cache.Thumbnail;
import com.github.jakz.geophoto.cache.ThumbSize;
import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.PhotoEnumeration;
import com.github.jakz.geophoto.ui.resources.Icons;
import com.pixbits.lib.functional.TriConsumer;
import com.pixbits.lib.lang.Pair;
import com.pixbits.lib.ui.table.DataSource;
import com.pixbits.lib.ui.table.ListModel;

public class PhotoGrid extends JPanel implements MultiPhotoView
{
  private final Mediator mediator;
  
  private final SearchField searchField;
  private final JSlider sizeSlider;
  private final JList<Photo> list;
  private final ListModel<Photo> model;
  
  private PhotoSelectionListener listener;
  private PhotoEnumeration photos;
  
  private int margin = 10;
  private ThumbSize thumbSize = ThumbSize.TINY;
  private final CellRenderer renderer = new CellRenderer();
    
  public PhotoGrid(Mediator mediator)
  {
    this.mediator = mediator;
    
    list = new JList<>();
    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    
    list.setCellRenderer(renderer);
    list.setBackground(UI.background);
    model = new ListModel<>(list, DataSource.empty());
    
    JScrollPane scrollPane = new JScrollPane(list);
    //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setPreferredSize(new Dimension(629,600));
    scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
    
    list.setVisibleRowCount(-1);    
    list.setFixedCellWidth(100);
    list.setFixedCellHeight(100);
        
    //TODO: should support changing the data
    
    sizeSlider = new JSlider();
    sizeSlider.setMinimum(ThumbSize.TINY.size + margin*2);
    sizeSlider.setMaximum(ThumbSize.LARGE.size + margin*2);
    sizeSlider.setMajorTickSpacing(100);
    sizeSlider.setSnapToTicks(true);
    
    sizeSlider.addChangeListener(e -> {
      int v = sizeSlider.getValue();
      list.setFixedCellHeight(v);
      list.setFixedCellWidth(v);
      
      for (ThumbSize size : ThumbSize.values())
        if (v - margin * 2 <= size.size)
        {
          thumbSize = size;
          break;
        }
    });
    
    searchField = new SearchField(this, mediator);
    
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    topPanel.add(searchField);
    topPanel.add(sizeSlider);
    
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
    add(topPanel, BorderLayout.NORTH);
    add(mediator.ui().statusBar(), BorderLayout.SOUTH);
  }

  private class RefreshDataCallback<T> implements TriConsumer<Photo, T, Boolean>
  {
    @Override
    public void accept(Photo t, T u, Boolean isNew)
    {
      mediator.ui().statusBar().taskDone();
      SwingUtilities.invokeLater(() -> refreshData());
    }  
  }
  
  private final RefreshDataCallback<Thumbnail> thumbnailLoadedCallback = new RefreshDataCallback<>();
  
  class CellRenderer extends DefaultListCellRenderer
  {
    private Photo photo;
    private Image image;
    private boolean isSelected;
    
    CellRenderer()
    {
      setPreferredSize(new Dimension(100,100));
      setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object v, int index, boolean isSelected, boolean cellHasFocus)
    {   
      this.photo = (Photo)v;
      this.isSelected = isSelected;
      
      try 
      {
        super.getListCellRendererComponent(list, v, index, isSelected, cellHasFocus);
        
        Pair<Thumbnail, Boolean> value = photo.thumbnails().asyncGet(mediator, thumbSize, thumbnailLoadedCallback);
        
        setVerticalTextPosition(SwingConstants.BOTTOM);        
        setHorizontalTextPosition(SwingConstants.CENTER);
        
        if (value.first != null)
          setText("");
        else 
          setText(photo.path().getFileName().toString());
        
        image = value.first != null ? value.first.image() : null;
        
        if (value.second)
          mediator.ui().statusBar().taskAdd();
        
      } 
      catch (IM4JavaException | InterruptedException | IOException e) 
      {
        e.printStackTrace();
      }
            
   
      
      return this;
    }
    
    @Override
    public void paintComponent(Graphics gx)
    {
      Graphics2D g = (Graphics2D)gx;
      g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

      final int w = getWidth(), h = getHeight();

      g.setBackground(isSelected ? UI.selectedBackground : UI.background);
      g.clearRect(2, 2, w-4, h-4);
      
      /*if (image != null)
      {
        final int iw = image.getWidth(null), ih = image.getHeight(null);
        
        int dx = w/2 - iw/2;
        int dy = h/2 - ih/2;
          
        g.drawImage(image, dx, dy, dx+iw, dy+ih, 0, 0, iw, ih, null);
      }*/
      
      if (image != null)
      {
        final int iw = image.getWidth(null), ih = image.getHeight(null);
        float ratio = iw / (float) ih;
        int dw, dh, dx, dy;
        
        if (iw > ih)
        {
          dw = w - (margin*2);
          dh = (int)((h - margin*2)/ratio);
          
          dx = margin;
          dy = margin + (h - margin*2 - dh) / 2;
          
        }
        else
        {
          dw =  (int)((w - margin*2)*ratio);
          dh = h - (margin*2);
          
          dx = margin + (w - margin*2 - dw) / 2;
          dy = margin;
        }
        
        g.drawImage(image, dx, dy, dx + dw, dy + dh, 0, 0, iw, ih, null);

        if (photo.isGeotagged())
        {
          final int iconSize = 16;
          g.drawImage(Icons.GPS_ICON, dx + dw - iconSize, dy + dh - iconSize, dx + dw, dy + dh, 0, 0, 32, 32, null);
        }
          
      }
      else
      {
        g.setColor(Color.RED);
        g.drawRect(margin, margin, w - margin*2, h - margin*2);
      }
    }
  };
  
  @Override
  public void filter(Predicate<Photo> filter)
  {
    if (photos != null)
    {
      photos.filter(filter);
      model.refresh();
    }
  }

  
  @Override
  public void setPhotos(PhotoEnumeration photos)
  {
    this.photos = photos;   
    
    photos.filter(searchField.predicate());
    list.clearSelection();
    list.ensureIndexIsVisible(0);
    model.setData(photos);
    model.refresh();
    

    list.getSelectionModel().removeListSelectionListener(listener);
    this.listener = new PhotoSelectionListener(mediator, photos);
    list.getSelectionModel().addListSelectionListener(listener);
  }
  
  @Override
  public void selectPhoto(Photo photo)
  {
    // TODO Auto-generated method stub
    
  }
  
  public void refreshData()
  {
    list.repaint();
    //model.fireTableDataChanged();
  }
}

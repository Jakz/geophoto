package com.github.jakz.geophoto.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.im4java.core.IM4JavaException;

import com.github.jakz.geophoto.Mediator;
import com.github.jakz.geophoto.cache.Thumbnail;
import com.github.jakz.geophoto.cache.ThumbnailSize;
import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.PhotoEnumeration;
import com.pixbits.lib.functional.TriConsumer;
import com.pixbits.lib.lang.Pair;
import com.pixbits.lib.ui.table.ListModel;

public class PhotoGrid extends JPanel
{
  private final Mediator mediator;
  
  private final JList<Photo> list;
  private final ListModel<Photo> model;
  
  public PhotoGrid(Mediator mediator, PhotoEnumeration photos)
  {
    this.mediator = mediator;
    
    list = new JList<>();
    list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    list.setCellRenderer(new CellRenderer());
    list.setBackground(new Color(214,217,223));
    model = new ListModel<Photo>(list, photos);
    
    JScrollPane scrollPane = new JScrollPane(list);
    //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setPreferredSize(new Dimension(629,600));
    scrollPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
    
    list.setVisibleRowCount(-1);    
    list.setFixedCellWidth(100);
    list.setFixedCellHeight(100);
    
    setLayout(new BorderLayout());
    add(scrollPane, BorderLayout.CENTER);
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
    CellRenderer()
    {
      setPreferredSize(new Dimension(100,100));
    }
    
    
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object v, int index, boolean isSelected, boolean cellHasFocus)
    {   
      Photo photo = (Photo)v;
      
      try 
      {
        super.getListCellRendererComponent(list, v, index, isSelected, cellHasFocus);
        
        Pair<Thumbnail, Boolean> value = photo.thumbnails().asyncGet(mediator, ThumbnailSize.TINY, thumbnailLoadedCallback);
        
        setVerticalTextPosition(SwingConstants.BOTTOM);        
        setHorizontalTextPosition(SwingConstants.CENTER);
        
        if (value.first != null)
          setIcon(new ImageIcon(value.first.image()));
        else 
          setIcon(null);
        
        if (value.second)
          mediator.ui().statusBar().taskAdd();
        
        setText(photo.path().getFileName().toString());
      } 
      catch (IM4JavaException | InterruptedException | IOException e) 
      {
        e.printStackTrace();
      }
            
   
      
      return this;
    }
  };
  
  
  public void refreshData()
  {
    list.repaint();
    //model.fireTableDataChanged();
  }
  
}

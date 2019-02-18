package com.github.jakz.geophoto.ui;

import java.util.function.Predicate;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.jakz.geophoto.Mediator;
import com.github.jakz.geophoto.data.Photo;

public class SearchField extends JPanel
{
  private final Mediator mediator;
  private final JTextField field;
  private final MultiPhotoView view;
  
  public SearchField(MultiPhotoView view, Mediator mediator)
  {
    this.mediator = mediator;
    this.view = view;
    
    field = new JTextField(10);
    field.addCaretListener(e -> refresh());
    
    add(field);
  }
  
  private void refresh()
  {
    view.filter(mediator.searcher().search(field.getText()));
  } 
  
  public Predicate<Photo> predicate()
  {
    return mediator.searcher().search(field.getText());
  }
}

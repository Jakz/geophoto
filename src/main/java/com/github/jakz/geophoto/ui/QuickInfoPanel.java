package com.github.jakz.geophoto.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.jakz.geophoto.data.Photo;

public class QuickInfoPanel extends JPanel
{
  JLabel iso;
  JLabel focalLength;
  JLabel fnumber;
  JLabel exposure;
  
  public QuickInfoPanel()
  {
    iso = new JLabel("ISO");
    focalLength = new JLabel("");
    fnumber = new JLabel("");
    exposure = new JLabel("");
    
    this.add(iso);
    this.add(focalLength);
    this.add(fnumber);
    this.add(exposure);
  }
  
  
  public void updateFor(Photo photo)
  {
    iso.setText("ISO "+photo.iso());
    focalLength.setText(photo.focalLength()+" mm");
    fnumber.setText("ƒ1/"+photo.fnumber());
    exposure.setText(""+photo.exposureTime());
  }
}

package com.github.jakz.geophoto.ui.resources;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Icons
{
  public static Image GPS_ICON;
  
  static
  {
    try
    {
      GPS_ICON = ImageIO.read(Icons.class.getResourceAsStream("has-gps-symbol.png"));
    } 
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}

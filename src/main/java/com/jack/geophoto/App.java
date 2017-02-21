package com.jack.geophoto;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import com.jack.geophoto.cache.ThumbnailCache;
import com.jack.geophoto.data.Coordinate;
import com.jack.geophoto.data.Photo;
import com.jack.geophoto.data.PhotoFolder;
import com.jack.geophoto.data.Size;
import com.jack.geophoto.reverse.Address;
import com.jack.geophoto.reverse.NominatimReverseGeocodingJAPI;
import com.jack.geophoto.tools.Exif;
import com.jack.geophoto.tools.ImageMagick;
import com.jack.geophoto.ui.PhotoTable;
import com.jack.geophoto.ui.UI;
import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.ui.UIUtils;
import com.pixbits.lib.util.ShutdownManager;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.events.ConsoleEvent;
import com.teamdev.jxbrowser.chromium.events.ConsoleListener;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import com.thebuzzmedia.exiftool.core.StandardTag;

/**
 * Hello world!
 *
 */
public class App 
{
  static ShutdownManager shutdown;
  
  static Browser browser;
  
  public static void main( String[] args )
  {    
    UIUtils.setNimbusLNF();
    
    Exif exif = new Exif(5);
    shutdown = new ShutdownManager(true);
    shutdown.addTask(() -> { try { exif.dispose(); } catch (Exception e) { } });

    //System.setProperty("exiftool.debug", "true");
       
    try
    {
      /*Coordinate c1 = new Coordinate(50.0359, -5.4253);
      Coordinate c2 = new Coordinate(58.3838, 3.0412);
      System.out.printf("Distance: %f, %f\n", c1.haversineDistance(c2), c1.cosineDistance(c2));*/
      
      PhotoFolder folder = new PhotoFolder(Paths.get(/*"/Volumes/Data/Photos/Organized/Vacanze/Cina '16"*/"./photos"));
      
      folder.findAllImages().forEach(StreamException.rethrowConsumer(p -> folder.add(new Photo(p))));
      folder.sort();
      
      UI.init(folder);

      
      folder.forEach(StreamException.rethrowConsumer(photo -> {
        exif.asyncFetch(photo, (p, er) -> {
          Coordinate coord = Coordinate.parse(er);
          p.coordinate(Coordinate.parse(er));
          if (coord.isValid())
            UI.map.addMarker(coord);
          UI.photoTable.refreshData();
            
          
        }, StandardTag.GPS_LATITUDE, StandardTag.GPS_LONGITUDE, StandardTag.GPS_ALTITUDE);
      }));

      /*if (true)
        return;
      
      folder.findAllImages().forEach(p ->{
        try
        {
          Coordinate coord = exif.loadCoordinate(new Photo(p));
          System.out.println(coord);
          
          if (coord.isValid())
          {
            NominatimReverseGeocodingJAPI reverse = new NominatimReverseGeocodingJAPI();
            Address address = reverse.getAdress(coord.lat(), coord.lng());
            System.out.println(address.getCountry()+", "+address.getCity());
          }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      });*/
      
      //exif.dispose();

      //im.createThumbnail(photo, new Size(400,200), Paths.get("./thumb.jpg"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    if (true)
      return;
  }
}

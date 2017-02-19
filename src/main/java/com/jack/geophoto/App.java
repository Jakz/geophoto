package com.jack.geophoto;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.jack.geophoto.data.Coordinate;
import com.jack.geophoto.data.Photo;
import com.jack.geophoto.data.PhotoFolder;
import com.jack.geophoto.data.Size;
import com.jack.geophoto.reverse.Address;
import com.jack.geophoto.reverse.NominatimReverseGeocodingJAPI;
import com.jack.geophoto.tools.ExifToolBridge;
import com.jack.geophoto.tools.ImageMagick;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.events.ConsoleEvent;
import com.teamdev.jxbrowser.chromium.events.ConsoleListener;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

/**
 * Hello world!
 *
 */
public class App 
{
  static Browser browser;
  
  public static void main( String[] args )
  {
    ExifToolBridge exif = new ExifToolBridge();
    
    Photo photo = null;
    try
    {
      /*Coordinate c1 = new Coordinate(50.0359, -5.4253);
      Coordinate c2 = new Coordinate(58.3838, 3.0412);
      System.out.printf("Distance: %f, %f\n", c1.haversineDistance(c2), c1.cosineDistance(c2));*/
      
      PhotoFolder folder = new PhotoFolder(Paths.get("./photos"));
      folder.findAllImages().forEach(p ->{
        System.out.println(p.toString());
      });
      
      if (true)
        return;
      
      
      photo = new Photo(Paths.get("./photos/P8191700.JPG"));
      photo.coordinate(exif.loadCoordinate(photo));
      
      ImageMagick im = new ImageMagick();
      
      BufferedImage img = im.createThumbnail(photo, new Size(400,200));
      
      ImageIO.write(img, "JPG", new File("./thumb-java.jpg"));
      
      System.out.println(img);
      
      //im.createThumbnail(photo, new Size(400,200), Paths.get("./thumb.jpg"));
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    if (true)
      return;

    browser = new Browser();
    BrowserView view = new BrowserView(browser);

    JFrame frame = new JFrame("JxBrowser Google Maps");
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.add(view, BorderLayout.CENTER);
    frame.setSize(1280, 800);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    
    browser.addConsoleListener(e -> System.out.println("Message: " + e.getMessage()));
    
    try
    {
      URL url = frame.getClass().getResource("/com/jack/geophoto/html/map.html");
      String path = new File(url.toURI()).getAbsolutePath();
      System.out.println("URL: "+path);
      browser.loadURL(url.toString());
      
      // 43.771389f,11.254167f
      
      Thread.sleep(1000);
      addMarker(photo.coordinate());
      
      NominatimReverseGeocodingJAPI reverse = new NominatimReverseGeocodingJAPI();
      Address address = reverse.getAdress(photo.coordinate().lat(), photo.coordinate().lng());
      System.out.println(address.getCountry()+", "+address.getCity());
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
  
  static void executeJavaScript(String script)
  {
    System.out.println("Executing JS:\n"+script+"\n");
    browser.executeJavaScript(script);
  }
  
  static void addMarker(Coordinate coordinate)
  {
    executeJavaScript(
      "var marker = new google.maps.Marker({ " +
      "position: { lat: " + coordinate.lat() + ", lng: " + coordinate.lng() + "}," +
      "map: map });"
    );
    
  }
}

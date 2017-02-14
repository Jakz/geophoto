package com.jack.geophoto;

import java.awt.BorderLayout;
import java.io.File;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import com.jack.geophoto.reverse.Address;
import com.jack.geophoto.reverse.NominatimReverseGeocodingJAPI;
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
      
      Thread.sleep(1000);
      addMarker(43.771389f,11.254167f);
      
      NominatimReverseGeocodingJAPI reverse = new NominatimReverseGeocodingJAPI();
      Address address = reverse.getAdress(43.771389f, 11.254167f);
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
  
  static void addMarker(float lat, float lng)
  {
    executeJavaScript(
      "var marker = new google.maps.Marker({ " +
      "position: { lat: " + lat + ", lng: " + lng + "}," +
      "map: map });"
    );
    
  }
}

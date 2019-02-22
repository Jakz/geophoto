package com.github.jakz.geophoto.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jxmapviewer.viewer.TileFactoryInfo;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class BingTileFactoryInfo extends TileFactoryInfo
{
  public BingTileFactoryInfo(String apiKey)
  {
    super("Bing Maps", 1, 21, 22, 256, true, true, "", "", "", "");
    
    try 
    {
      String urlBase = "http://dev.virtualearth.net/REST/V1/Imagery/Metadata/Road?output=xml&include=ImageryProviders&key=";
      urlBase += "AlCfIyys5bK--F8WHQvIc5NBVl8OrprwbdnrNb2P6h5h1pNmO8V5a1xDnAHTha12";
      URL url = new URL(urlBase);
      
      URLConnection request = url.openConnection();
      request.connect();

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setValidating(true);
      factory.setIgnoringElementContentWhitespace(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      
      Document doc = builder.parse(request.getInputStream());
      
      
    } 
    catch (IOException | ParserConfigurationException | SAXException e)
    {
      e.printStackTrace();
    }
  }
  
  private final String[] subdomains = new String[] { "t0", "t1", "t2", "t3" };
  private final String tileUrl = "http://ecn.%s.tiles.virtualearth.net/tiles/r%s.jpeg?g=6897&mkt=%s&shading=hill";

  private String computeQuadKey(int x, int y, int zoom)
  {
    StringBuilder quadKey = new StringBuilder();  
    
    for (int i = zoom; i > 0; i--)  
    {  
        char digit = '0';  
        int mask = 1 << (i - 1);  
        
        if ((x & mask) != 0)  
        {  
            digit++;  
        }  
        
        if ((y & mask) != 0)  
        {  
            digit++;  
            digit++;  
        }  
        
        quadKey.append(digit);  
    }  
    
    return quadKey.toString();  
  }
  
  @Override public String getBaseURL() { throw new IllegalArgumentException("invalid operation on TileFactoryInfo"); }
  
  public String getTileUrl(int x, int y, int zoom)
  {
    String quadKey = computeQuadKey(x, y, zoom);
    String url = String.format(tileUrl, subdomains[0], quadKey, "en-US");
    return url;
  }
}

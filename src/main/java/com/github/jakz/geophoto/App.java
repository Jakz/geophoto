package com.github.jakz.geophoto;

import java.nio.file.Paths;
import com.github.jakz.geophoto.data.Coordinate;
import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.PhotoFolder;
import com.github.jakz.geophoto.tools.Exif;
import com.github.jakz.geophoto.ui.UI;
import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.ui.UIUtils;
import com.pixbits.lib.util.ShutdownManager;
import com.thebuzzmedia.exiftool.core.StandardTag;


/**
 * Hello world!
 *
 */
public class App 
{
  static ShutdownManager shutdown;
      
  public static void main( String[] args )
  {    
    UIUtils.setNimbusLNF();
    
    /*Path qr = Paths.get("/Volumes/Vicky/Photos-SSD/GPX/Cina '16/qr/10-1/P8100623.JPG");
    try
    {
      byte[] data = ZXing.readQRCode(qr);
      Path out = Paths.get("./qrcode.bin");
      DataOutputStream dos = new DataOutputStream(Files.newOutputStream(out));
      dos.write(data);
      dos.close();
    } 
    catch (NotFoundException | IOException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    
    
    if (true)
      return;*/
    
    
    Exif exif = new Exif(5);
    shutdown = new ShutdownManager(true);
    shutdown.addTask(() -> { try { exif.dispose(); } catch (Exception e) { } });

    //System.setProperty("exiftool.debug", "true");
       
    try
    {      
      /*Coordinate c1 = new Coordinate(50.0359, -5.4253);
      Coordinate c2 = new Coordinate(58.3838, 3.0412);
      System.out.printf("Distance: %f, %f\n", c1.haversineDistance(c2), c1.cosineDistance(c2));*/
      
      //PhotoFolder folder = new PhotoFolder(Paths.get("/Volumes/Data/Photos/Organized/Vacanze/Normandia '17"));
      PhotoFolder folder = new PhotoFolder(Paths.get("./photos"));

      
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

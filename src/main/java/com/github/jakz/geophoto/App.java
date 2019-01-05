package com.github.jakz.geophoto;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import com.github.jakz.geophoto.data.Photo;
import com.github.jakz.geophoto.data.PhotoEnumeration;
import com.github.jakz.geophoto.data.PhotoFolder;

import com.github.jakz.geophoto.reverse.GeoReversePool;
import com.github.jakz.geophoto.reverse.NominatimReverseGeocodingJAPI;
import com.github.jakz.geophoto.tools.Exif;
import com.github.jakz.geophoto.ui.UI;
import com.github.jakz.geophoto.ui.tree.TreeBuilder;
import com.pixbits.lib.functional.StreamException;
import com.pixbits.lib.io.xml.gpx.Coordinate;
import com.pixbits.lib.ui.UIUtils;
import com.pixbits.lib.util.ShutdownManager;
import com.thebuzzmedia.exiftool.core.StandardTag;

public class App 
{
  static ShutdownManager shutdown;
  public static Mediator mediator = new MyMediator();
      
  public static void main( String[] args )
  {    
    /*Gpx gpx;
    try
    {
      gpx = GpxParser.parse(Paths.get("./photos/data.gpx"));         
      GpxParser.save(gpx, Paths.get("./photos/data-out.gpx"));
    } 
    catch (IOException | SAXException | JAXBException e)
    {
      e.printStackTrace();
    }   
    
    if (true)
      return;*/
    
    

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
    
    UIUtils.setNimbusLNF();
    
    try
    {
      mediator.init();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    Exif<Photo> exif = new Exif<>(5);


    //System.setProperty("exiftool.debug", "true");
    
    /*try
    {
      PhotoFolder folder = new PhotoFolder(Paths.get("/Volumes/OSX Dump/Photos/Vacanze/Norvegia '18/A7"), false);
      folder.findAllImages().forEach(StreamException.rethrowConsumer(p -> folder.add(new Photo(p))));
      folder.sort();

      System.out.printf("Found %d photos\n", folder.size());
      
      final Map<String,Map<String,Integer>> focals = new HashMap<>();
      
      AtomicInteger counter = new AtomicInteger();
      
      folder.forEach(StreamException.rethrowConsumer(photo -> {
        exif.asyncFetch(photo, (p, er) -> {
          String model = er.get(StandardTag.MODEL).toString();
          String lens = er.get(StandardTag.LENS_MODEL).toString();
          String focal = er.get(StandardTag.FOCAL_LENGTH).toString();
          
          System.out.println(counter.getAndIncrement()+" of "+folder.size()+": "+model+", "+p.path().toString());
          
          if (model.equals("ILCE-7RM3"))
          {            
            Map<String, Integer> focalsByLens = focals.computeIfAbsent(lens, l -> new TreeMap<>());
            focalsByLens.compute(focal, (f, o) -> o == null ? 1 : (o+1));
          }
        }, StandardTag.FOCAL_LENGTH, StandardTag.MODEL, StandardTag.LENS_MODEL);
      }));
      
      while (counter.get() < 1585);

      focals.forEach((lens, focalsByLens) -> {
        System.out.println("Lens "+lens);
        focalsByLens.forEach((focal, count) -> System.out.println(focal+", "+count));
      });
      
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    
    if (true)
      return;*/
    
    try
    {      
      /*Coordinate c1 = new Coordinate(50.0359, -5.4253);
      Coordinate c2 = new Coordinate(58.3838, 3.0412);
      System.out.printf("Distance: %f, %f\n", c1.haversineDistance(c2), c1.cosineDistance(c2));*/
      
      Path path = Paths.get("/Volumes/OSX Dump/Photos/Vacanze/Islanda '17");
      Set<Photo> photos = mediator.scanner().findAllPhotosInFolder(path);
      
           
      PhotoFolder folder = new PhotoFolder(path, photos);
           
      folder.sort();
  
      UI ui = mediator.ui();
      ui.init(folder);
      
      
      List<Photo> current = new ArrayList<Photo>();
      List<PhotoEnumeration> splits = new ArrayList<>();
      for (int i = 0; i < folder.size(); ++i)
      {
        if ((i % 500) == 0 && i > 0)
        {
          splits.add(PhotoEnumeration.of(current, "Test "+i));
          current = new ArrayList<Photo>();
        }
        
        current.add(folder.get(i));
      }
      
      if (!current.isEmpty())
        splits.add(PhotoEnumeration.of(current, "Test last"));

      ui.treeView().setRoot(TreeBuilder.ofFlatList(splits));
      
      
      folder.forEach(StreamException.rethrowConsumer(photo -> {
        Coordinate c = mediator.pdatabase().getCoordinateForPhoto(photo);
        
        if (c != null)
          photo.coordinate(c);
        else
        {
          exif.asyncFetch(photo, StreamException.rethrowBiConsumer((p, er) -> {
            Coordinate coord = Exif.parseGpxTags(er);
            p.coordinate(coord);
            if (coord.isValid())
            {
              mediator.pdatabase().setCoordinateForPhoto(photo, coord);
              ui.map.addMarker(coord, photo);
            }
            ui.photoTable.refreshData();
              
            
          }), StandardTag.GPS_LATITUDE, StandardTag.GPS_LONGITUDE, StandardTag.GPS_ALTITUDE);
        }
        
        

      }));
      
      {
        exif.waitUntilFinished();
        
        /*GeoReversePool reversePool = new GeoReversePool(new NominatimReverseGeocodingJAPI(), 2);
        
        folder.forEach(p -> {
          if (p.coordinate() != null && !p.coordinate().isUnknown())
          {
            reversePool.submit(p, (ph,g) -> { 
              if (g != null)
              {
                ph.geocode(g);
                UI.photoTable.refreshData();
              }
            });
          }
        });*/
      }

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

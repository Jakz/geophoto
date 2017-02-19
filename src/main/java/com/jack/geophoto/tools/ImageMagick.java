package com.jack.geophoto.tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.MogrifyCmd;
import org.im4java.core.Stream2BufferedImage;

import com.jack.geophoto.data.Photo;
import com.jack.geophoto.data.Size;

public class ImageMagick
{
  ConvertCmd convert;
  
  public ImageMagick()
  {
    Path mogrifyPath = Paths.get("./tools/imagemagick/bin");
    convert = new ConvertCmd();
    convert.setSearchPath(mogrifyPath.toString());
  }
  
  public BufferedImage createThumbnail(Photo photo, Size size) throws IM4JavaException, InterruptedException, IOException
  {
    IMOperation operation = new IMOperation();
    
    operation.addImage();
    operation.thumbnail(size.width);
    operation.autoOrient();
    operation.format("jpg");
    operation.addImage("-");
    
    Stream2BufferedImage stream = new Stream2BufferedImage();
    convert.setOutputConsumer(stream);
        
    System.out.println("Starting to convert: "+operation);
    convert.run(operation, photo.path().toString());
    System.out.println("Finished");
    
    return stream.getImage();
  }
  
  public void createThumbnail(Photo photo, Size size, Path output) throws IM4JavaException, InterruptedException, IOException
  {
    IMOperation operation = new IMOperation();
    
    operation.addImage();
    operation.thumbnail(size.width);
    operation.autoOrient();
    operation.format("jpg");
    operation.addImage();
        
    System.out.println("Starting to convert: "+operation);
    convert.run(operation, photo.path().toString(), output.toString());
    System.out.println("Finished");
    
    
  }
}

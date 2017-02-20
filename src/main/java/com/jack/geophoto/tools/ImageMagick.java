package com.jack.geophoto.tools;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.core.Stream2BufferedImage;
import org.im4java.process.ProcessEvent;
import org.im4java.process.ProcessEventListener;
import org.im4java.process.ProcessExecutor;
import org.im4java.process.ProcessTask;

import com.jack.geophoto.Log;
import com.jack.geophoto.data.Photo;
import com.jack.geophoto.data.Size;

public class ImageMagick
{
  private final ProcessExecutor pool;
  private final ConvertCmd convert;
  private final Path TOOL_PATH = Paths.get("./tools/imagemagick/bin");
  
  public ImageMagick(int poolSize)
  {
    pool = new ProcessExecutor(poolSize);
    
    convert = new ConvertCmd();
    convert.setSearchPath(TOOL_PATH.toString());
  }
  
  public void dispose()
  {
    pool.shutdownNow();
  }
  
  protected ConvertCmd convert()
  {
    ConvertCmd convert = new ConvertCmd();
    convert.setSearchPath(TOOL_PATH.toString()); 
    return convert;
  }
  
  protected void submit(ImageCommand cmd, IMOperation operation, Object... args) throws IOException, IM4JavaException
  {
    ProcessTask task = cmd.getProcessTask(operation, args);
    pool.submit(task);
  }
  
  public void createThumbnail(Photo photo, Size size, BiConsumer<Photo, BufferedImage> callback) throws IM4JavaException, InterruptedException, IOException
  { 
    ConvertCmd command = convert();
    
    IMOperation operation = new IMOperation();
    
    operation.define(String.format("jpeg:size=%dx%d", size.width*2, size.height*2));
    operation.addImage();
    operation.thumbnail(size.width, size.height);
    operation.unsharp(0.5);
    operation.autoOrient();
    operation.format("jpg");
    operation.addImage("-");
    
    Stream2BufferedImage stream = new Stream2BufferedImage();
    command.setOutputConsumer(stream);
    command.addProcessEventListener(new MemoryThumbnailGenerationListener(photo, stream, callback));

    
    System.out.println("Starting to convert: "+operation);
    submit(command, operation, photo.path().toString());
    System.out.println("Finished");
  }
  
  public void createThumbnail(Photo photo, Size size, Path output) throws IM4JavaException, InterruptedException, IOException
  {
    IMOperation operation = new IMOperation();
    
    operation.define(String.format("jpeg:size=%dx%d", size.width*2, size.height*2));
    operation.addImage();
    operation.thumbnail(size.width, size.height);
    operation.unsharp(0.5);
    operation.autoOrient();
    operation.format("jpg");
    operation.addImage();
        
    System.out.println("Starting to convert: "+operation);
    submit(null, operation, photo.path().toString(), output.toString());
    System.out.println("Finished"); 
  }
  
  public void waitForAllTasks() throws InterruptedException
  {
    pool.shutdown();
    pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
  }
  
  private static class MemoryThumbnailGenerationListener implements ProcessEventListener
  {
    private final Photo photo;
    private final Stream2BufferedImage stream;
    private final BiConsumer<Photo, BufferedImage> consumer;
    
    MemoryThumbnailGenerationListener(Photo photo, Stream2BufferedImage stream, BiConsumer<Photo, BufferedImage> consumer)
    {
      this.photo = photo;
      this.stream = stream;
      this.consumer = consumer;
    }
    
    @Override
    public void processInitiated(ProcessEvent e)
    {

    }

    @Override
    public void processStarted(ProcessEvent e)
    {
      
    }

    @Override
    public void processTerminated(ProcessEvent e)
    {      
      if (e.getReturnCode() == 0)
        consumer.accept(photo, stream.getImage());
    }
    
  }
}

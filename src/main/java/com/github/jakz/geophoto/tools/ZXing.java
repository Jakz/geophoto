package com.github.jakz.geophoto.tools;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

public class ZXing
{
  public static byte[] readQRCode(Path path) throws FileNotFoundException, NotFoundException, IOException
  {
    Map<DecodeHintType, ?> hintMap = new HashMap<>();
    return readQRCode(path, hintMap);
  }
  
  public static byte[] readQRCode(Path path, Map<DecodeHintType, ?> hintMap) throws FileNotFoundException, IOException, NotFoundException 
  {
    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
        new BufferedImageLuminanceSource(
            ImageIO.read(new BufferedInputStream(Files.newInputStream(path))))));
    
    Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
    return qrCodeResult.getRawBytes();
  }
}

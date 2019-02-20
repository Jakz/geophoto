package com.github.jakz.geophoto.faces;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class Test
{
 
  public static void main(String[] args)
  {
    nu.pattern.OpenCV.loadLocally();
    
    CascadeClassifier faceDetector = new CascadeClassifier("lbpcascade_frontalface.xml");
    Mat image = Imgcodecs.imread("/Users/jack/Documents/Dev/eclipse/geophoto/photos/P8110825.JPG");

    MatOfRect faceDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);

    System.out.println(String.format("Faces detected: %s ", faceDetections.toArray().length));

    int i = 0;
    for (Rect rect : faceDetections.toArray()) {
      //Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0), 3);  
      
      Mat face = new Mat(image, rect);
      Imgcodecs.imwrite("face"+i+".jpg", face);   
      ++i;
    }
  }
}

package com.github.jakz.geophoto.data;

public class Point
{
  public final double x;
  public final double y;
  public final double z;
  
  public Point(double x, double y)
  {
    this.x = x;
    this.y = y;
    this.z = 0.0f;
  }
  
  public Point(double x, double y, double z)
  {
    this.x = x;
    this.y = y;
    this.z = z;
  }
  
  public Point sum(Point other)
  {
    return new Point(this.x + other.x, this.y + other.y, this.z + other.z);
  }
  
  public Point divide(double value)
  {
    return new Point(this.x/value, this.y/value, this.z/value);
  }
  
}

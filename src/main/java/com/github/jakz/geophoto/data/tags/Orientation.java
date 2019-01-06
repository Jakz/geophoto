package com.github.jakz.geophoto.data.tags;

public enum Orientation
{
  NORMAL(0, "Normal"),
  FLIP_HORIZONTAL(1, "Flip Horizontal"),
  ROTATE_CCW180(2, "Rotated 180"),
  FLIP_VERTICAL(3, "Flip Vertical"),
  TRANSPOSE(4, "Transposed"),
  ROTATE_CCW90(5, "Rotated 90"),
  TRANSVERSE(6, "Transverse"),
  ROTATE_CCW270(7, "Roated 270"),
  
  NONE(-1, "None"),

  ;
  
  public String caption;
  public int value;
  
  private Orientation(int value, String caption)
  {
    this.caption = caption;
    this.value = value;
  }
  
  public String toString() { return caption; }

  public static Orientation get(int v) { return values()[v]; }
}

package com.github.jakz.geophoto.data;

public class Rational
{
  public final long num, den;
  
  public Rational(String string)
  {
    int index = string.indexOf("/");   
    this.num = Long.parseLong(string, 0, index, 10);
    this.den = Long.parseLong(string, index+1, string.length(), 10);
  }
  
  public Rational(long num, long den)
  {
    this.num = num;
    this.den = den;
  }
  
  public String toString() { return num + "/" + den; }
}

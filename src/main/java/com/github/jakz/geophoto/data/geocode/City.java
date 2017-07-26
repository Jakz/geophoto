package com.github.jakz.geophoto.data.geocode;

public class City
{
  private final String name;
  
  public City(String name)
  {
    this.name = name;
  }
  
  public String name() { return name; }
  
  @Override public String toString() { return name; }
}

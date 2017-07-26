package com.github.jakz.geophoto.data.geocode;

import java.util.Locale;
import java.util.Objects;

public class Country
{
  private final Locale locale;
  
  private Country(Locale locale)
  {
    this.locale = locale;
  }
  
  public Country(String countryCode)
  {
    this.locale = new Locale("", countryCode);
  }
  
  @Override
  public String toString()
  {
    return locale.getDisplayCountry();
  }
  
  public static final Country UNKNOWN = new Country((Locale)null)
  {
    @Override public boolean equals(Object object) { return UNKNOWN == this; }
    @Override public String toString() { return "Unknown"; }
  };
}

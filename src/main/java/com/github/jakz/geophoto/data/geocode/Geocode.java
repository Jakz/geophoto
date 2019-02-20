package com.github.jakz.geophoto.data.geocode;

import java.util.Optional;

import com.github.jakz.geophoto.reverse.Address;

public class Geocode
{
  private Country country;
  private String state;
  private String region;
  private String county;
  private String neighbourhood;
  //private String footway;
  //private String 
  
  private Optional<City> city;
  private String fullName;

  public final Address address;
  
  public Geocode(Address address)
  {
    this.address = address;

    this.country = Country.UNKNOWN;
    this.city = Optional.empty();
    
    if (address != null)
    {
      if (address.getCountryCode() != null)
        this.country = new Country(address.getCountryCode().toUpperCase());

      if (address.getCity() != null)
        this.city = Optional.of(new City(address.getCity()));

    }
  }
  
  public Optional<City> city() { return city; }
  public Country country() { return country; }
    
  @Override
  public String toString()
  {
    return country+", " + address.getCity();
  }
 
  public static Geocode UNKNOWN = new Geocode(null) { 
    @Override public boolean equals(Object object) { return object == this; }
  }; 
}

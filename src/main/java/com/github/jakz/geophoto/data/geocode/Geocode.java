package com.github.jakz.geophoto.data.geocode;

import java.util.Optional;

import com.github.jakz.geophoto.reverse.Address;

public class Geocode
{
  private final Country country;
  private final Optional<City> city;
  
  public final Address address;
  
  public Geocode(Address address)
  {
    this.address = address;
    
    if (address != null && address.getCountryCode() != null)
      this.country = new Country(address.getCountryCode().toUpperCase());
    else
      this.country = Country.UNKNOWN;
    
    if (address != null && address.getCity() != null)
      this.city = Optional.of(new City(address.getCity()));
    else
      this.city = Optional.empty();
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

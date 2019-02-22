package com.github.jakz.geophoto.data.geocode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.jakz.geophoto.reverse.Address;

public class Geocode
{
  private Country country;
  private String name;
  private String displayName;
  
  private String category;
  private String type;
  
  private Map<String, String> address;
  
  public Geocode(JSONObject json)
  {    
    if (json == null)
      return;
    
    country = Country.UNKNOWN;
    address = new HashMap<>();
                
    try
    {
      name = !json.isNull("name") ? json.getString("name") : null;
      displayName = json.getString("display_name");
      category = json.getString("category");
      type = json.getString("type");
      
      JSONObject address = json.getJSONObject("address");
      
      if (address != null)
      {
        country = address.has("country_code") ? new Country(address.getString("country_code").toUpperCase()) : Country.UNKNOWN;
        
        
        Iterator it = address.keys();
        while (it.hasNext())
        {
          String key = (String)it.next();
          address.put(key, address.getString(key));
        }
      }
    }
    catch (JSONException e)
    {
      e.printStackTrace();
    }
  }
  
  public String city() { return address.get("city"); }
  public Country country() { return country; }
    
  @Override
  public String toString()
  {
    return name+", "+category+", "+type+" ("+country+")";
  }
 
  public static Geocode UNKNOWN = new Geocode(null) { 
    @Override public boolean equals(Object object) { return object == this; }
  }; 
}

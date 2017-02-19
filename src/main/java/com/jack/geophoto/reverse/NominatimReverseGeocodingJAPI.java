/*
 * (C) Copyright 2014 Daniel Braun (http://www.daniel-braun.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.jack.geophoto.reverse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

/**
 * Java library for reverse geocoding using Nominatim
 * 
 * @author Daniel Braun
 * @version 0.1
 *
 */
public class NominatimReverseGeocodingJAPI {
	private final String NominatimInstance = "http://nominatim.openstreetmap.org"; 

	private int zoomLevel = 18;
	private String locale = "en-us";
		
	public static void main(String[] args){
		if(args.length < 1){
			System.out.println("use -help for instructions");
		}
		else if(args.length < 2){
			if(args[0].equals("-help")){
				System.out.println("Mandatory parameters:");
				System.out.println("   -lat [latitude]");
				System.out.println("   -lon [longitude]");
				System.out.println ("\nOptional parameters:");
				System.out.println("   -zoom [0-18] | from 0 (country) to 18 (street address), default 18");
				System.out.println("   -osmid       | show also osm id and osm type of the address");
				System.out.println("\nThis page:");
				System.out.println("   -help");
			}
			else
				System.err.println("invalid parameters, use -help for instructions");
		}
		else{
			boolean latSet = false;
			boolean lonSet = false;
			boolean osm = false;
			
			double lat = -200;
			double lon = -200;
			int zoom = 18;
			
			for (int i = 0; i < args.length; i++) {
				if(args[i].equals("-lat")){					
					try{  
					    lat = Double.parseDouble(args[i+1]);  
					}  
					catch(NumberFormatException nfe){  
					    System.out.println("Invalid latitude");
					    return;
					}  
					
					latSet = true;
					i++;
					continue;
				}		
				else if(args[i].equals("-lon")){
					try{  
					    lon = Double.parseDouble(args[i+1]);  
					}  
					catch(NumberFormatException nfe){  
					    System.out.println("Invalid longitude");
					    return;
					} 
					
					lonSet = true;
					i++;
					continue;
				}
				else if(args[i].equals("-zoom")){
					try{  
					    zoom = Integer.parseInt(args[i+1]);  
					}  
					catch(NumberFormatException nfe){  
					    System.out.println("Invalid zoom");
					    return;
					} 
					
					i++;
					continue;
				}
				else if(args[i].equals("-osm")){
					osm = true;
				}
				else{
					System.err.println("invalid parameters, use -help for instructions");
					return;
				}
			}
			
			if(latSet && lonSet){
				NominatimReverseGeocodingJAPI nominatim = new NominatimReverseGeocodingJAPI(zoom);
				Address address = nominatim.getAdress(lat, lon);
				System.out.println(address);
				if(osm){
					System.out.print("OSM type: " + address.getOsmType()+", OSM id: " + address.getOsmId());
				}
			}
			else{
				System.err.println("please specifiy -lat and -lon, use -help for instructions");
			}			
		}				
	}
	
	public NominatimReverseGeocodingJAPI() { }
	
	public NominatimReverseGeocodingJAPI(int zoomLevel){
		if(zoomLevel < 0 || zoomLevel > 18){
			System.err.println("invalid zoom level, using default value");
			zoomLevel = 18;
		}
		
		this.zoomLevel = zoomLevel;
	}
	
	public Address getAdress(double lat, double lon){
		Address result = null;		
		String urlString = NominatimInstance + "/reverse?format=json&addressdetails=1&lat=" + String.valueOf(lat) + "&lon=" + String.valueOf(lon) + "&zoom=" + zoomLevel + "&accept-language=" + locale;
		try {
			result =new Address(getJSON(urlString), zoomLevel);
		} catch (IOException e) {
			System.err.println("Can't connect to server.");
			e.printStackTrace();
		}		
		return result;
	}
	
	private String getJSON(String urlString) throws IOException{
		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		String json = IOUtils.toString(is, "UTF-8"); 
		is.close();		
		return json;
	}
}
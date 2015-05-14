package com.example.vaavudproject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.R.integer;

public class WeatherHttpClient {
	private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?lat=";
	private String uLon = "&lon=";
     
    public String getWeatherData(double lat,double lon) {
        HttpURLConnection con = null ;
        InputStream is = null;
        
        try {
            con = (HttpURLConnection) ( new URL(BASE_URL + lat + uLon + lon)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
             
            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");
             
            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }
 
        return null;
                 
    }
  }

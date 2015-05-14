package com.example.vaavudproject;

import org.json.JSONException;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {
	
	private AppLocationService appLocationService;
	private ProgressDialog pDialog;
	private WeatherHttpClient whc;
	private Weather weather;
	private double longitude;
	private double latitude;
	
	private TextView cityText;
	private TextView condDescr;
	private TextView temp;
	private TextView press;
	private TextView windSpeed;
	private TextView windDeg;
	
	private TextView hum;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        whc = new WeatherHttpClient();
        
        appLocationService = new AppLocationService(
        		MainActivity.this);
        
        Location gpsLocation = appLocationService
				.getLocation(LocationManager.GPS_PROVIDER);
        
        Location nwLocation = appLocationService
				.getLocation(LocationManager.NETWORK_PROVIDER);
        
        cityText = (TextView) findViewById(R.id.cityText);
		condDescr = (TextView) findViewById(R.id.condDescr);
		temp = (TextView) findViewById(R.id.temp);
		hum = (TextView) findViewById(R.id.hum);
		press = (TextView) findViewById(R.id.press);
		windSpeed = (TextView) findViewById(R.id.windSpeed);
		windDeg = (TextView) findViewById(R.id.windDeg);

		if (nwLocation != null) {
			latitude = nwLocation.getLatitude();
			longitude = nwLocation.getLongitude();
			Toast.makeText(
					getApplicationContext(),
					"Mobile Location (NW): \nLatitude: " + latitude
							+ "\nLongitude: " + longitude,
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getApplicationContext(),"sorry", Toast.LENGTH_LONG).show();
		}

		JSONWeatherTask task = new JSONWeatherTask();
	    task.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class JSONWeatherTask extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... arg0) {
			weather = new Weather();
		    String data = ( (new WeatherHttpClient()).getWeatherData(latitude,longitude));
		 
		    try {
		        weather = JSONWeatherParser.getWeather(data);
		         
		        // Let's retrieve the icon
		         
		    } catch (JSONException e) {               
		        e.printStackTrace();
		    }
		    return null;
		}
		
		@Override
		protected void onPostExecute(String file_url) {	
			
			cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
			condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
			temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "degC");
			hum.setText("" + weather.currentCondition.getHumidity() + "%");
			press.setText("" + weather.currentCondition.getPressure() + " hPa");
			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
			windDeg.setText("" + weather.wind.getDeg() + "deg");
				
		}
    
    }
}

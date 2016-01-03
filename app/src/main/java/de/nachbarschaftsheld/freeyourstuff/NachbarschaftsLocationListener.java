package de.nachbarschaftsheld.freeyourstuff;

import android.content.Context;
import android.content.ContextWrapper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by nico on 28/12/15.
 */
public class NachbarschaftsLocationListener implements LocationListener {

    private Context context;

    public NachbarschaftsLocationListener(Context context){
        this.context = context;
    }

    @Override
    public void onLocationChanged(Location location) {
        MainActivity.currentLocation = location;
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Log.v(MainActivity.TAG, "Location changed " + location.toString());
        //latitude.setText("Latitude: "+String.valueOf(location.getLatitude()));

        //longitude.setText("Longitude: "+String.valueOf(location.getLongitude()));
        //provText.setText(provider + " provider has been selected.");

        /*------- To get city name from coordinates -------- */
        String cityName = null;
        String countryName = null;
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude,
                    longitude, 1);
            if (addresses.size() > 0) {
                cityName = addresses.get(0).getLocality();
                countryName = addresses.get(0).getCountryName();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(MainActivity.TAG, longitude + "\t" + latitude + "\tCity: "
                + cityName+ "\tCountry: "
                + countryName);
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(MainActivity.TAG, "Provider " + provider + " disabled!");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(MainActivity.TAG, "Provider " + provider + " enabled!");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(MainActivity.TAG, provider + "'s status changed to "+ status );
    }
}
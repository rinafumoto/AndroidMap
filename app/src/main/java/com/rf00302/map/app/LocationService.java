package com.rf00302.map.app;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Service class to update the current location.
 */
public class LocationService extends Service {

    /**
     * default constructor.
     */
    public LocationService() {
    }

    /**
     * override onStartCommand method to get current location c
     * permission is requested in Map.
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1024, 500, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent i = new Intent("update");
                i.putExtra( "latitude",location.getLatitude() );
                i.putExtra( "longitude", location.getLongitude() );
                sendBroadcast( i );
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}

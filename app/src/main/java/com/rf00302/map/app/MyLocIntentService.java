package com.rf00302.map.app;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.support.annotation.Nullable;

/**
 * Intent Service to get last known location in background.
 */
public class MyLocIntentService extends IntentService {

    private String provider;

    /**
     * default constructor.
     */
    public MyLocIntentService() {
        super( MyLocIntentService.class.getName() );
    }

    /**
     * override onHandleIntent method to get last known locationthe location of the camera and destination.
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        LocationManager locationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        try {
            /**
             * use criteria to get best location estimate.
             */
            Criteria locCriteria = new Criteria();
            locCriteria.setAccuracy( Criteria.ACCURACY_FINE );
            provider = locationManager.getBestProvider( locCriteria, false );
            double lat = locationManager.getLastKnownLocation( provider ).getLatitude();
            double lng = locationManager.getLastKnownLocation( provider ).getLongitude();
            Intent i = new Intent("myLoc");
            i.putExtra( "latitude",lat );
            i.putExtra( "longitude", lng );
            sendBroadcast( i );
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }
}

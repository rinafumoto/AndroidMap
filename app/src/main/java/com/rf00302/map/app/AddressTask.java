package com.rf00302.map.app;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.IOException;
import java.util.List;

/**
 * AsyncTask to get latitude and longitude from the address in background.
 */
public class AddressTask extends AsyncTask<String, Void, List<Address>> {

    private Context context;
    private Spinner destinations;
    private String destination;
    private Database db;
    private String address;

    /**
     * parameterised constructor to define context, database, spinner, and string.
     * @param context
     * @param destinations
     * @param destination
     */
    public AddressTask(Context context, Spinner destinations, String destination) {
        super();
        this.context = context;
        db = new Database( context );
        this.destinations = destinations;
        this.destination = destination;
    }

    /**
     * override doInBackground method to get location from address
     * @param strings
     * @return addresses
     */
    @Override
    protected List<Address> doInBackground(String... strings) {
        address = strings[0];
        Geocoder coder = new Geocoder( context );
        List<Address> addresses = null;
        try {
            addresses = coder.getFromLocationName( address, 1 );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addresses;
    }

    /**
     * override onPostExecute method to add the location into the database and dropdown list.
     * @param addresses
     */
    @Override
    protected void onPostExecute(List<Address> addresses) {
        if (addresses != null) {
            Address location = addresses.get( 0 );
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            db.addDestination( destination, lat, lng );
            if (db.getNames() != null) {
                ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>( context, android.R.layout.simple_spinner_item, db.getNames() );
                citiesAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
                destinations.setAdapter( citiesAdapter );
            }
        }
    }
}

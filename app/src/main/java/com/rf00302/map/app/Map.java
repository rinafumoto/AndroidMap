package com.rf00302.map.app;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * this activity is called when GO button is clicked.
 * this activity displays a map.
 */
public class Map extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap map;
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private BroadcastReceiver myLocReceiver;
    private IntentFilter myLocFilter;
    private LatLng loc;
    private LatLng des;
    private String destination;
    private Database db;
    private LatLng camera;
    private Button getDistance;
    private Button saveBtn;

    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    /**
     * override onCreate method to initialize the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_map );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        /**
         * initialize the map fragment and call onMapReady when loaded.
         */
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById( R.id.map );
        mapFragment.getMapAsync( this );

        /**
         * initialize the database.
         */
        db = new Database( this );

        /**
         * get a string from MainActivity.
         */
        Intent intent = getIntent();
        destination = intent.getStringExtra( "destination" );

        /**
         * initialize intentfilters.
         */
        filter = new IntentFilter( "update" );
        myLocFilter = new IntentFilter( "myLoc" );

        /**
         * check permission and request if needed.
         */
        if (ContextCompat.checkSelfPermission( this,
                Manifest.permission.ACCESS_FINE_LOCATION )
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions( this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION );
        } else {

        }

        /**
         * call LocationService.
         */
        Intent i = new Intent( Map.this, LocationService.class );
        startService( i );

        /**
         * call MyLocationService.
         */
        Intent ii = new Intent( Map.this, MyLocIntentService.class );
        startService( ii );

        /**
         * initialize button and set listener.
         * when the button is clicked, get distance between current location and destination and call DistanceActivity if it is portrait.
         * Otherwise, display the distance on the right side of the screen by using update method in the fragment class.
         */
        getDistance = (Button) findViewById( R.id.distanceBtn );
        getDistance.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (des != null) {
                    float results[] = new float[1];
                    Location.distanceBetween( loc.latitude, loc.longitude, des.latitude, des.longitude, results );
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        Intent disintent = new Intent( Map.this, DistanceActivity.class );
                        disintent.putExtra( "distance", results[0] );
                        disintent.putExtra( "destination", destination );
                        startActivity( disintent );
                    } else {
                        DistanceFragment fragment = (DistanceFragment) getFragmentManager().findFragmentById( R.id.distancefrag );
                        fragment.update( destination, results[0] );
                    }
                }
            }
        } );

        /**
         * initialize button and set listener.
         * update the location in the database when the button is clicked.
         */
        saveBtn = (Button)findViewById( R.id.saveBtn );
        saveBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (des != null) {
                    db.update( destination,des.latitude, des.longitude );
                }
            }
        } );
    }

    /**
     * override onRestoreInstanceState to restore the location of the camera and destination.
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        if (savedInstanceState != null) {
            double lat = savedInstanceState.getDouble( "lat" );
            double lng = savedInstanceState.getDouble( "lng" );
            camera = new LatLng( lat, lng );
            double marklat = savedInstanceState.getDouble( "marklat" );
            double marklng = savedInstanceState.getDouble( "marklng" );
            des = new LatLng( marklat, marklng );
        }
    }

    /**
     * override onResume method to initialize the broadcastreceivers and register them.
     */
    @Override
    protected void onResume() {
        super.onResume();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                loc = new LatLng( intent.getDoubleExtra( "latitude", 0 ), intent.getDoubleExtra( "longitude", 0 ) );
                setLine();
            }
        };
        registerReceiver( receiver, filter );
        myLocReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (loc == null) {
                    loc = new LatLng( intent.getDoubleExtra( "latitude", 0 ), intent.getDoubleExtra( "longitude", 0 ) );
                    setLine();
                }
            }
        };
        registerReceiver( myLocReceiver, myLocFilter );
    }

    /**
     * override onMapReady method to initialize the map, call onMapLoaded when layout done and set listener.
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        this.map.setOnMapLoadedCallback( this );
        this.map.setOnMarkerDragListener( this );
    }

    /**
     * set elements on the map and enable zoom control
     */
    @Override
    public void onMapLoaded() {
        getDestination();
        setLine();
        setCamera();
        map.getUiSettings().setZoomControlsEnabled( true );
    }

    /**
     * method to get location of the destination from database.
     */
    private void getDestination() {
        if (des == null) {
            des = db.getDestination( destination );
        }
    }

    /**
     * method to set line between current location and destination.
     */
    private void setLine() {
        if (loc != null && des != null) {
            map.clear();
            map.addMarker( new MarkerOptions().position( loc ).title( "You are here" ) );
            map.addMarker( new MarkerOptions().position( des ).title( destination ).draggable( true ) );
            map.addPolyline( new PolylineOptions()
                    .add( loc )
                    .add( des )
            );
        }
    }

    /**
     * method to set camera.
     * this method moves camera to destination at the beginning and stay at where the user moved to.
     */
    private void setCamera() {
        if (des != null) {
            if (camera != null) {
                map.moveCamera( CameraUpdateFactory.newLatLngZoom( camera, 15 ));
            } else {
                map.moveCamera( CameraUpdateFactory.newLatLngZoom( des,15 ) );
            }
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        des = marker.getPosition();
        setLine();
    }

    /**
     * override onSaveInstanceState method to save the location of the camera and destination.
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState( outState );
        outState.putDouble( "lat", map.getCameraPosition().target.latitude );
        outState.putDouble( "lng", map.getCameraPosition().target.longitude );
        outState.putDouble( "marklat", des.latitude );
        outState.putDouble( "marklng", des.longitude );
    }

    /**
     * override onPause method to unregister the receivers.
     */
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver( receiver );
        unregisterReceiver( myLocReceiver );
    }
}
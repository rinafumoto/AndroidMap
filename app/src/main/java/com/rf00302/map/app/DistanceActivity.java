package com.rf00302.map.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * this activity is called when the GET DISTANCE is clicked when the orientation is portrait.
 * this activity is to display the distance between current location and the destination.
 */
public class DistanceActivity extends AppCompatActivity {

    private TextView destination;
    private TextView distance;
    private float dist;
    private String dest;

    /**
     * override onCeate method  to display the distance.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_distance );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        /**
         * initialize text views.
         */
        destination = (TextView)findViewById( R.id.distanceText );
        distance = (TextView)findViewById( R.id.distance );

        /**
         * get the distance and the name of the destination.
         */
        Intent intent = getIntent();
        dist = intent.getFloatExtra( "distance" , 0);
        dest = intent.getStringExtra( "destination" );

        /**
         * convert the distance into km.
         */
        float newDist = dist / 1000;

        /**
         * set texts.
         */
        destination.setText( dest );
        distance.setText( Float.toString( newDist ) + "km" );

    }

}

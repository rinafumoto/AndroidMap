package com.rf00302.map.app;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * this is a subclass which represent the fragment to distance
 */
public class DistanceFragment extends Fragment {

    private TextView destination;
    private TextView distance;

    /**
     * default constructor.
     */
    public DistanceFragment() {
    }

    /**
     * this method is executed when the instance of this class is created.
     * this method inflate the layout for this fragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return inflater.inflate( R.layout.fragment_display, container, false )
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_distance, container, false );
    }

    /**
     * this method initializes the TextView
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );

        destination = (TextView)getActivity().findViewById( R.id.distanceText );
        distance = (TextView)getActivity().findViewById( R.id.distance );
    }

    /**
     * this method set texts.
     * @param dest
     * @param dist
     */
    public void update(String dest, float dist) {
        float newDist = dist / 1000;
        destination.setText( dest );
        distance.setText( Float.toString( newDist ) + "km" );
    }
}

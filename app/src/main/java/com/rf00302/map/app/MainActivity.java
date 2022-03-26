package com.rf00302.map.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * the main activity which is executed when the app opens.
 * this activity allows user to add new destinations and delete them.
 */
public class MainActivity extends AppCompatActivity {

    private Spinner destinations;
    private Button go;
    private String destination;
    private EditText name;
    private EditText add;
    private Button addBtn;
    private Button removeBtn;
    private Database db;

    /**
     * override onCreate method which initialize the activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        /**
         * initialize database and widgets.
         */
        db = new Database( this );
        name = (EditText)findViewById( R.id.name );
        add = (EditText)findViewById( R.id.address );
        addBtn = (Button)findViewById( R.id.addBtn );
        removeBtn = (Button)findViewById(R.id.removeBtn );
        go = (Button)findViewById( R.id.go );
        destinations = (Spinner) findViewById(R.id.cities);
        addItemToCitiesSpinner();
        addListenerToCitiesSpinner();

        /**
         * call AddressTask when the ADD button is clicked.
         */
        addBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddressTask( MainActivity.this, destinations,name.getText().toString() ).execute( add.getText().toString() );
            }
        } );

        /**
         * remove the chosen destination from database and dropdown list when the REMOVE button is clicked.
         */
        removeBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destination != null) {
                    db.removeDestination( destination );
                    addItemToCitiesSpinner();
                }
            }
        } );

        /**
         * call Map with passing the name of the destination when the GO button is clicked.
         */
        go.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (destination != null) {
                    Intent intent = new Intent( MainActivity.this, Map.class );
                    intent.putExtra( "destination", destination );
                    startActivity( intent );
                }
            }
        } );
    }

    /**
     * get the names of destinations saved in database and set into the dropdown list.
     */
    private void addItemToCitiesSpinner() {
        if (db.getNames() != null) {
            ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_item, db.getNames() );
            citiesAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
            destinations.setAdapter( citiesAdapter );
        }
    }

    /**
     * it is executed when the item is selected from the dropdown and get the selected destination.
     */
    public void addListenerToCitiesSpinner() {
        destinations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                destination = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
    }

}

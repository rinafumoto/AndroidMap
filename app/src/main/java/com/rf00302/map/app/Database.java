package com.rf00302.map.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * this class helps to store data in database.
 */
public class Database extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "cities";
    private static final String COL1 = "name";
    private static final String COL2 = "lat";
    private static final String COL3 = "lng";
    private SQLiteDatabase db = this.getWritableDatabase();

    /**
     * parameterised constuctor.
     * @param context
     */
    public Database(Context context) {
        super( context, TABLE_NAME, null, 1 );
    }

    /**
     * this method is executed when this class is called.
     * this method creates a table to store the data of the destinations
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COL1 + " TEXT PRIMARY KEY, " +
                COL2 + " DOUBLE, " +
                COL3 + " DOUBLE)";
        try {
            sqLiteDatabase.execSQL( createTable );
        } catch (SQLException e) {
            Log.v( "TAG", "could not create table", e );
        }
    }

    /**
     * this method is executed when the app is upgraded.
     * thismethod drop the table and create a new empty table.
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate( sqLiteDatabase );
    }

    /**
     * this method is to store the data of the destination in the table.
     * @param name
     * @param lat
     * @param lng
     */
    public void addDestination(String name, double lat, double lng) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = \"" + name + "\"";
        Cursor data = db.rawQuery( query, null );
        if (!data.moveToNext()) {
            ContentValues contentValues = new ContentValues();
            contentValues.put( COL1, name );
            contentValues.put( COL2, lat );
            contentValues.put( COL3, lng );

            db.insert( TABLE_NAME, null, contentValues );
        }
    }

    /**
     * this method is to get location from the name of the destination.
     * @param name
     * @return
     */
    public LatLng getDestination(String name) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = \"" + name + "\"";
        Cursor data = db.rawQuery( query, null );
        data.moveToFirst();
        return new LatLng( data.getDouble( 1 ), data.getDouble( 2 ) );
    }

    /**
     * this method is to get all the names of the destinations in the table.
     * @return
     */
    public List<String> getNames() {
        List<String> list = new ArrayList<String>();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME;
        Cursor data = db.rawQuery( query, null );
        while (data.moveToNext()) {
            list.add( data.getString( 0 ) );
        }
        return list;
    }

    /**
     * this method is to remove the destination from the table.
     * @param name
     */
    public void removeDestination(String name) {
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + " = \"" + name + "\"";
        db.execSQL( query );
    }

    /**
     * this method is to update the location.
     * @param name
     * @param lat
     * @param lng
     */
    public void update(String name, double lat, double lng) {
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 + " = " + lat + ", " + COL3 + " = " + lng + " WHERE " + COL1 + " = \"" + name + "\"";
        db.execSQL( query );
    }
}
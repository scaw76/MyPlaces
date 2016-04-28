package com.saracawley.myplaces.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.saracawley.myplaces.Place;

import java.util.UUID;

/**
 * Created by sara on 4/26/2016.
 */
public class PlaceCursorWrapper extends CursorWrapper {

    public PlaceCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Place getPlace(){
        String uuidString = getString(getColumnIndex(PlaceDBSchema.PlaceTable.Cols.UUID));
        String name = getString(getColumnIndex(PlaceDBSchema.PlaceTable.Cols.NAME));
        String lat = getString(getColumnIndex(PlaceDBSchema.PlaceTable.Cols.LAT));
        String lon = getString(getColumnIndex(PlaceDBSchema.PlaceTable.Cols.LON));

        Place p = new Place(UUID.fromString(uuidString));
        p.setName(name);
        p.setLat(lat);
        p.setLon(lon);

        return p;
    }
}

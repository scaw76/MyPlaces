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

        Place p = new Place(UUID.fromString(uuidString));
        p.setName(name);

        return p;
    }
}

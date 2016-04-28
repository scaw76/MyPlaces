package com.saracawley.myplaces;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.saracawley.myplaces.database.PlaceBaseHelper;
import com.saracawley.myplaces.database.PlaceCursorWrapper;
import com.saracawley.myplaces.database.PlaceDBSchema.PlaceTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sara on 4/26/2016.
 */
public class PlaceManger {
    private static PlaceManger sPlaceManger;

    //private List<Place> mPlaces;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static PlaceManger get(Context context) {
        if(sPlaceManger == null){
            sPlaceManger = new PlaceManger(context);
        }
        return sPlaceManger;
    }

    private PlaceManger(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new PlaceBaseHelper(mContext)
                .getWritableDatabase();
        //mPlaces = new ArrayList<>();
    }

    public List<Place> getPlaces(){
        List<Place> places = new ArrayList<>();
        PlaceCursorWrapper cursor = queryPlaces(null, null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                places.add(cursor.getPlace());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }
        return places;
    }

    public Place getPlace(UUID id){
        PlaceCursorWrapper cursor = queryPlaces(
                PlaceTable.Cols.UUID + " = ? ", new String[]{id.toString()}
        );
        try {
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
             return cursor.getPlace();
        }finally {
            cursor.close();
        }
    }
    public void addPlace(Place p){
        //mPlaces.add(p);
        ContentValues values = getContentValues(p);
        mDatabase.insert(PlaceTable.NAME, null, values);
    }

    public void updatePlace(Place p){
        String uuidString = p.getID().toString();
        ContentValues values = getContentValues(p);
        mDatabase.update(PlaceTable.NAME, values,PlaceTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    public void deletePlace(Place p){
        //mPlaces.remove(p);
        mDatabase.delete(PlaceTable.NAME, PlaceTable.Cols.UUID + " =? ", new String[] { p.getID().toString() });
        File file = getPhotoFile(p);

        if(file != null){
            boolean deleted = file.delete();
            Log.i("PlaceManager", "deleted " + deleted);
        }
    }
    private static ContentValues getContentValues(Place place){
        ContentValues values = new ContentValues();
        values.put(PlaceTable.Cols.UUID, place.getID().toString());
        values.put(PlaceTable.Cols.NAME, place.getName());

        return values;
    }
    private PlaceCursorWrapper queryPlaces(String whereClause, String [] whereArgs){
        Cursor cursor = mDatabase.query(
                PlaceTable.NAME, null, whereClause,whereArgs, null,null,null
        );
        return new PlaceCursorWrapper(cursor);
    }
    public File getPhotoFile(Place p){
        File externalFileDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(externalFileDir == null){
            return null;
        }
        return new File(externalFileDir, p.getPhotoFileName());
    }
}

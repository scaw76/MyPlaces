package com.saracawley.myplaces.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.saracawley.myplaces.database.PlaceDBSchema.*;

/**
 * Created by sara on 4/26/2016.
 */
public class PlaceBaseHelper extends SQLiteOpenHelper{
    private static final int VERSION =1;
    private static final String DATABASE_NAME = "placeBase.db";

    public PlaceBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ PlaceTable.NAME +"("+
                "_id integer primary key autoincrement, " +
                PlaceTable.Cols.UUID +", "+
                PlaceTable.Cols.NAME + ", "+
                PlaceTable.Cols.LAT + ", "+
                PlaceTable.Cols.LON + ")"

        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

package com.saracawley.myplaces;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by sara on 4/26/2016.
 */
public class PlaceManger {
    private static PlaceManger sPlaceManger;

    private List<Place> mPlaces;

    public static PlaceManger get(Context context) {
        if(sPlaceManger == null){
            sPlaceManger = new PlaceManger(context);
        }
        return sPlaceManger;
    }

    private PlaceManger(Context context) {
        mPlaces = new ArrayList<>();
        /*
        for(int i = 0; i<100 ;i++){
            Place place = new Place();
            place.setName("place # " + i);
            mPlaces.add(place);
        }
        */

    }

    public List<Place> getPlaces(){
        return mPlaces;
    }

    public Place getPlace(UUID id){
        for(Place p : mPlaces){
            if(p.getID().equals(id)){
                return p;
            }
        }
        return null;
    }
    public void addPlace(Place p){
        mPlaces.add(p);
    }
    public void deletePlace(Place p){
        mPlaces.remove(p);
    }
}

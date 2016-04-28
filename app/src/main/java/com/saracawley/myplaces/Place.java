package com.saracawley.myplaces;

import java.util.UUID;

/**
 * Created by sara on 4/26/2016.
 */
public class Place {
    private UUID mID;
    private String mName;
    private String mPhoto;
    private String mLat;
    private String mLon;

    public Place() {
        mID = UUID.randomUUID();
    }
    public Place(UUID id){
        mID = id;

    }

    public UUID getID() {
        return mID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public String getLat() {
        return mLat;
    }

    public void setLat(String lat) {
        mLat = lat;
    }

    public String getLon() {
        return mLon;
    }

    public void setLon(String lon) {
        mLon = lon;
    }

    public String getPhotoFileName(){
        return "IMG_" +getID().toString()+".jpg";
    }

}

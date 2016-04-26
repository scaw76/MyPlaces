package com.saracawley.myplaces;

import java.util.UUID;

/**
 * Created by sara on 4/26/2016.
 */
public class Place {
    private UUID mID;
    private String mName;
    private String mPhoto;
    private double mLat;
    private double mLon;

    public Place() {
        mID = UUID.randomUUID();
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

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        mLon = lon;
    }
}

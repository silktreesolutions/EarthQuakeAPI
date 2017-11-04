package com.example.android.quakereport;


import java.util.Date;

/**
 * Created by admin on 30-Aug-2017.
 */

public class EarthQuake {

    //to hold earthquake magnitutde
    private double mEarthquakeMagnitude;
    //to hold earthquake location
    private String mEarthquakeLocation;
    //to hold the earthquake date and time
    private long mEarthquakeDateAndTime;
    //to hold the earthquake url
    private String mEarthquakeurl;

    public EarthQuake(double earthquakeMagnitutde, String earthquakeLocation, long earthquakeDateAndTime, String earthquakeURL){

        mEarthquakeMagnitude = earthquakeMagnitutde;
        mEarthquakeLocation = earthquakeLocation;
        mEarthquakeDateAndTime = earthquakeDateAndTime;
        mEarthquakeurl = earthquakeURL;
    }

    //getter method for earthquake magnitutde
    public double getEarthquakeMagnitude(){
        return mEarthquakeMagnitude;
    }

    //getter method for earthquake location
    public String getEarthquakeLocation(){
        return mEarthquakeLocation;
    }

    //getter method for earthquake date and time
    public long getEarthquakeDateAndTime(){
        return mEarthquakeDateAndTime;
    }

    //getter method for earthquake url
    public String getEarthQuakeURL(){
        return mEarthquakeurl;
    }
}

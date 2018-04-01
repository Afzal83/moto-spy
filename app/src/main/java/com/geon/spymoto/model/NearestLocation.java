package com.geon.spymoto.model;

/**
 * Created by Babu on 1/7/2017.
 */

public class NearestLocation {


    public  String PlaceName = "";
    public  double  Latitude = 0.0;
    public  double Longitude = 0.0;
    public  double distance = 0.0;

    public NearestLocation(String PlaceName,double Latitude,double Longitude,double distance){
        this.PlaceName = PlaceName;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.distance = distance;
    }
}

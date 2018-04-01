package com.geon.spymoto.helper;

import android.util.Log;

import com.geon.spymoto.model.LocationData;
import com.google.android.gms.maps.model.LatLng;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Babu on 7/25/2017.
 */

public class MapHelper {
    public static double CalculateDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.e("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }
    public static LatLng midPoint(double lat1, double lon1, double lat2, double lon2){

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);


        return new LatLng(Math.toDegrees(lat3), Math.toDegrees(lon3));

    }
    public  static double calculateDistance(List<LocationData> locationdata){
        double dis = 0.0;
        for(int i=0; i<locationdata.size(); i++){
            if(i>0){
                LatLng fromLatLong = new LatLng(locationdata.get(i-1).getLatitudeDbl(),locationdata.get(i-1).getLontitudeDbl());
                LatLng toLatLong = new LatLng(locationdata.get(i).getLatitudeDbl(),locationdata.get(i).getLontitudeDbl());
                dis += CalculateDistance(fromLatLong,toLatLong);
            }
        }
        return Math.round(dis*100.0)/100.0;
    }
}

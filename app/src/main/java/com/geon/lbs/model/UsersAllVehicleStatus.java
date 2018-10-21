package com.geon.lbs.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Babu on 7/18/2017.
 */
/*
public class UsersAllVehicleStatus {
    public double Latitude = 0.0;
    public double Longitude = 0.0 ;
    public double Speed = 0.0;
    public String DataStatus = "";
    public String EngineStatus = "";
    public String ACStatus = "";
    public String deviceImei = "";
    public String dataTime = "";
    public String vehicleNumberPlate = "";

    public double getLatitude(){
        return this.Latitude;
    }
    public double getLongitude(){
        return  this.Longitude;
    }
    public String getVehicleNumberPlate(){
        return this.vehicleNumberPlate;
    }
}*/
public  class UsersAllVehicleStatus implements Parcelable {
    public double Latitude = 0.0;
    public double Longitude = 0.0 ;
    public double Speed = 0.0;
    public String DataStatus = "";
    public String EngineStatus = "";
    public String ACStatus = "";
    public String deviceImei = "";
    public String dataTime = "";
    public String vehicleNumberPlate = "";

    public UsersAllVehicleStatus() {

    }
    public UsersAllVehicleStatus(double Latitde,double Longitude,double speed,String DataStatus,
                            String EngineStatus,String ACStatus,String deviceImei,
                                 String dataTime,String vehicleNumberPlate){

        this.Latitude = Latitde;
        this.Longitude = Longitude;
        this.Speed = speed;
        this.DataStatus = DataStatus;
        this.EngineStatus = EngineStatus;
        this.ACStatus = ACStatus;
        this.deviceImei = deviceImei;
        this.dataTime = dataTime;
        this.vehicleNumberPlate =vehicleNumberPlate;

    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeDouble(Latitude);
        out.writeDouble(Longitude);
        out.writeDouble(Speed);
        out.writeString(DataStatus);
        out.writeString(EngineStatus);
        out.writeString(ACStatus);
        out.writeString(deviceImei);
        out.writeString(dataTime);
        out.writeString(vehicleNumberPlate);
    }
    private UsersAllVehicleStatus(Parcel in) {
        Longitude = in.readDouble();
        Longitude = in.readDouble();
        Speed = in.readDouble();
        DataStatus = in.readString();
        ACStatus = in.readString();
        deviceImei = in.readString();
        dataTime = in.readString();
        vehicleNumberPlate = in.readString();
        EngineStatus = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<UsersAllVehicleStatus> CREATOR
            = new Creator<UsersAllVehicleStatus>() {
        @Override
        public UsersAllVehicleStatus createFromParcel(Parcel in) {
            return new UsersAllVehicleStatus(in);
        }
        @Override
        public UsersAllVehicleStatus[] newArray(int size) {
            return new UsersAllVehicleStatus[size];
        }
    };
}

package com.geon.spymoto.model;

/**
 * Created by Babu on 6/11/2017.
 */

public class VehicleInfo {
    public String vehicleId = "";
    public String vehicleNumberPlate = "";
    public String vehicleCode = "";
    public String speedLimit = "";
    public String loadStatus = "";
    public String callBackSim = "";
    public String imeiNumber = "";

    public VehicleInfo(){

    }
    public VehicleInfo(String vehicleId, String vehicleNumberPlate , String vehicleCode,
                       String speedLimit,String loadStatus ,String callBackSim ,String imeiNumber){

        this.vehicleId = vehicleId;
        this.vehicleNumberPlate = vehicleNumberPlate;
        this.vehicleCode = vehicleCode;
        this.speedLimit = speedLimit;
        this.loadStatus = loadStatus;
        this.callBackSim = callBackSim;
        this.imeiNumber = imeiNumber;
    }

}

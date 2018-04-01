package com.geon.spymoto.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by Babu on 3/8/2018.
 */


@Parcel
public class VehicleStatus {

    private String numberPlate="";
    private double latDouble ;
    private double longitudeDouble;

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("device_emei")
    private String device_emei;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("speed")
    private String speed;

    @SerializedName("engine_status")
    private String engine_status;

    @SerializedName("ac_status")
    private String ac_status;

    @SerializedName("record_time")
    private String record_time;

    @SerializedName("data_status")
    private String data_status;


    public double getLatDouble() {
        return Double.parseDouble(latitude);
    }

    public void setLatDouble(double latDouble) {
        this.latDouble = latDouble;
    }

    public double getLongitudeDouble() {
        return Double.parseDouble(longitude);
    }

    public void setLongitudeDouble(double longitudeDouble) {
        this.longitudeDouble = longitudeDouble;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getDevice_emei() {
        return device_emei;
    }

    public void setDevice_emei(String device_emei) {
        this.device_emei = device_emei;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getEngine_status() {
        return engine_status;
    }

    public void setEngine_status(String engine_status) {
        this.engine_status = engine_status;
    }

    public String getAc_status() {
        return ac_status;
    }

    public void setAc_status(String ac_status) {
        this.ac_status = ac_status;
    }

    public String getRecord_time() {
        return record_time;
    }

    public void setRecord_time(String record_time) {
        this.record_time = record_time;
    }

    public String getData_status() {
        return data_status;
    }

    public void setData_status(String data_status) {
        this.data_status = data_status;
    }
}

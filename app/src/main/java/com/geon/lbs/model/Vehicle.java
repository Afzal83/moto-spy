package com.geon.lbs.model;

import com.google.gson.annotations.SerializedName;

public class Vehicle {

    @SerializedName("vehicle_id")
    private String vehicle_id;

    @SerializedName("number_plate")
    private String number_plate;

    @SerializedName("vehicle_code")
    private String vehicle_code;

    @SerializedName("speed_limit")
    private String speed_limit;

    @SerializedName("is_loaded")
    private int is_loaded;

    @SerializedName("call_back_sim")
    private String call_back_sim;

    @SerializedName("emei_number")
    private String emei_number;

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getNumber_plate() {
        return number_plate;
    }

    public void setNumber_plate(String number_plate) {
        this.number_plate = number_plate;
    }

    public String getVehicle_code() {
        return vehicle_code;
    }

    public void setVehicle_code(String vehicle_code) {
        this.vehicle_code = vehicle_code;
    }

    public String getSpeed_limit() {
        return speed_limit;
    }

    public void setSpeed_limit(String speed_limit) {
        this.speed_limit = speed_limit;
    }

    public int getIs_loaded() {
        return is_loaded;
    }

    public void setIs_loaded(int is_loaded) {
        this.is_loaded = is_loaded;
    }

    public String getCall_back_sim() {
        return call_back_sim;
    }

    public void setCall_back_sim(String call_back_sim) {
        this.call_back_sim = call_back_sim;
    }

    public String getEmei_number() {
        return emei_number;
    }

    public void setEmei_number(String emei_number) {
        this.emei_number = emei_number;
    }
}

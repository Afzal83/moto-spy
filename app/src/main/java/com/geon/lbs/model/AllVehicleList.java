package com.geon.lbs.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllVehicleList {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("vehicle_list")
    private List<Vehicle> vehicle_list = null;

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

    public List<Vehicle> getVehicle_list() {
        return vehicle_list;
    }

    public void setVehicle_list(List<Vehicle> vehicle_list) {
        this.vehicle_list = vehicle_list;
    }
}

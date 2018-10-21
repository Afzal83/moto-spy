package com.geon.lbs.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Babu on 3/8/2018.
 */

public class AllVehicleStatus {

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("vehicles_status")
    private List<VehicleStatus> vehicles_status = null;

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

    public List<VehicleStatus> getVehicles_status() {
        return vehicles_status;
    }

    public void setVehicles_status(List<VehicleStatus> vehicles_status) {
        this.vehicles_status = vehicles_status;
    }
}

package com.geon.spymoto.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Babu on 3/22/2018.
 */

public class HistoryData {
    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("record_list")
    private List<LocationData> record_list = null;

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

    public List<LocationData> getRecord_list() {
        return record_list;
    }

    public void setRecord_list(List<LocationData> record_list) {
        this.record_list = record_list;
    }
}

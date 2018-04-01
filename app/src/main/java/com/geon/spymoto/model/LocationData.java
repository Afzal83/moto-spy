package com.geon.spymoto.model;


import com.google.gson.annotations.SerializedName;

public class LocationData {

    double latitudeDbl = 0.0;
    double lontitudeDbl = 0.0;
    double speedDbl = 0.0;


    @SerializedName("id")
    private String id;

    @SerializedName("device_emei")
    private String device_emei;

    @SerializedName("record_date")
    private String record_date;

    @SerializedName("record_time")
    private String record_time;

    @SerializedName("data_status")
    private String data_status;

    @SerializedName("engine_status")
    private String engine_status;

    @SerializedName("speed")
    private String speed;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("location_address")
    private String location_address;

    @SerializedName("n_s_indicator")
    private String n_s_indicator;

    @SerializedName("e_w_indicator")
    private String e_w_indicator;

    @SerializedName("bearing")
    private String bearing;

    @SerializedName("direction")
    private String direction;

    @SerializedName("ac_status")
    private String ac_status;

    @SerializedName("fuel_connection_status")
    private String fuel_connection_status;

    @SerializedName("gps_tracking_status")
    private String gps_tracking_status;

    @SerializedName("alarm_status")
    private String alarm_status;

    @SerializedName("alarm_type")
    private String alarm_type;

    @SerializedName("charge_status")
    private String charge_status;

    @SerializedName("defence_status")
    private String defence_status;

    @SerializedName("voltage_level")
    private String voltage_level;

    @SerializedName("gsm_signal_strength")
    private String gsm_signal_strength;

    @SerializedName("alarm_language")
    private String alarm_language;


    public double getLatitudeDbl() {
        return Double.parseDouble(latitude);
    }

    public double getLontitudeDbl() {
        return Double.parseDouble(longitude);
    }

    public double getSpeedDbl() {
        return Double.parseDouble(speed);
    }


    public void setLatitudeDbl(double latitudeDbl) {
        this.latitudeDbl = latitudeDbl;
    }

    public void setLontitudeDbl(double lontitudeDbl) {
        this.lontitudeDbl = lontitudeDbl;
    }

    public void setSpeedDbl(double speedDbl) {
        this.speedDbl = speedDbl;
    }

    public String getSpeed() {
        return speed;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice_emei() {
        return device_emei;
    }

    public void setDevice_emei(String device_emei) {
        this.device_emei = device_emei;
    }

    public String getRecord_date() {
        return record_date;
    }

    public void setRecord_date(String record_date) {
        this.record_date = record_date;
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

    public String getEngine_status() {
        return engine_status;
    }

    public void setEngine_status(String engine_status) {
        this.engine_status = engine_status;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    public String getN_s_indicator() {
        return n_s_indicator;
    }

    public void setN_s_indicator(String n_s_indicator) {
        this.n_s_indicator = n_s_indicator;
    }

    public String getE_w_indicator() {
        return e_w_indicator;
    }

    public void setE_w_indicator(String e_w_indicator) {
        this.e_w_indicator = e_w_indicator;
    }

    public String getBearing() {
        return bearing;
    }

    public void setBearing(String bearing) {
        this.bearing = bearing;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAc_status() {
        return ac_status;
    }

    public void setAc_status(String ac_status) {
        this.ac_status = ac_status;
    }

    public String getFuel_connection_status() {
        return fuel_connection_status;
    }

    public void setFuel_connection_status(String fuel_connection_status) {
        this.fuel_connection_status = fuel_connection_status;
    }

    public String getGps_tracking_status() {
        return gps_tracking_status;
    }

    public void setGps_tracking_status(String gps_tracking_status) {
        this.gps_tracking_status = gps_tracking_status;
    }

    public String getAlarm_status() {
        return alarm_status;
    }

    public void setAlarm_status(String alarm_status) {
        this.alarm_status = alarm_status;
    }

    public String getAlarm_type() {
        return alarm_type;
    }

    public void setAlarm_type(String alarm_type) {
        this.alarm_type = alarm_type;
    }

    public String getCharge_status() {
        return charge_status;
    }

    public void setCharge_status(String charge_status) {
        this.charge_status = charge_status;
    }

    public String getDefence_status() {
        return defence_status;
    }

    public void setDefence_status(String defence_status) {
        this.defence_status = defence_status;
    }

    public String getVoltage_level() {
        return voltage_level;
    }

    public void setVoltage_level(String voltage_level) {
        this.voltage_level = voltage_level;
    }

    public String getGsm_signal_strength() {
        return gsm_signal_strength;
    }

    public void setGsm_signal_strength(String gsm_signal_strength) {
        this.gsm_signal_strength = gsm_signal_strength;
    }

    public String getAlarm_language() {
        return alarm_language;
    }

    public void setAlarm_language(String alarm_language) {
        this.alarm_language = alarm_language;
    }
}

package com.geon.lbs.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Babu on 9/28/2016.
 */
public class User {

    boolean isUserLoogedIn =false;
    String userPassword = "";

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("user_type")
    private String user_type;

    @SerializedName("name_first")
    private String name_first;

    @SerializedName("name_last")
    private String name_last;

    @SerializedName("email")
    private String email;

    @SerializedName("contact_mobile")
    private String contact_mobile;

    public boolean isUserLoogedIn() {
        return isUserLoogedIn;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setUserLoogedIn(boolean userLoogedIn) {
        isUserLoogedIn = userLoogedIn;
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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getName_first() {
        return name_first;
    }

    public void setName_first(String name_first) {
        this.name_first = name_first;
    }

    public String getName_last() {
        return name_last;
    }

    public void setName_last(String name_last) {
        this.name_last = name_last;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact_mobile() {
        return contact_mobile;
    }

    public void setContact_mobile(String contact_mobile) {
        this.contact_mobile = contact_mobile;
    }
}

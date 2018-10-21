package com.geon.lbs.serviceapi;

/**
 * Created by Babu on 3/7/2018.
 */

public abstract class Callback<T> {
    public abstract void onSuccess(T response);
    public abstract void onError(String s) ;
}
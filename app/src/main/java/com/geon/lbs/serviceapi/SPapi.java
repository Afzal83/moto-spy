package com.geon.lbs.serviceapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Babu on 3/29/2018.
 */

public class SPapi {

    SharedPreferences sharedPref;
    Context mContext;

    public SPapi(Context mContext){
        this.mContext = mContext;
        sharedPref =  PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
    }
    public void saveStrngToSharedPreference(String key,String value){
        SharedPreferences.Editor editor =  sharedPref.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public String getStringFromSharedPreference(String key){
        return sharedPref.getString(key,"");
    }
    public void saveBoolToSharedPreference(String key,boolean value){
        SharedPreferences.Editor editor =  sharedPref.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public boolean getBoolFromSharedPreference(String key){
        return  sharedPref.getBoolean(key,false);
    }
}

package com.geon.lbs.serviceapi;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.geon.lbs.AppGlobal;
import com.geon.lbs.helper.Constants;
import com.geon.lbs.services.GeocodeAddressIntentService;
import com.geon.lbs.ui.fragment.MapFragment;


public class RequestLocationAddressApi {

    private Context mContext;
    private AppGlobal mGlobals;

    public RequestLocationAddressApi(Context mContext){
        this.mContext = mContext;
        mGlobals = ((AppGlobal)mContext.getApplicationContext());
    }

    public void requestLocationAddress(final double lat,final double lng,final Callback<String> callback){
        mGlobals.thread_for_livetracking_api=true;
        Runnable runnableCodeToDownloadVehicleStatus = new Runnable() {
            @Override
            public void run() {

                if(mContext==null){
                    return;
                }
                startLocationAddressService(lat,lng,callback);
            }
        };
        new Handler().post(runnableCodeToDownloadVehicleStatus);
    }
    private void startLocationAddressService(double lat,double lng,final Callback mCollback){
        ResultReceiver mReceiver=new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, final Bundle resultData) {
                if (resultCode == Constants.SUCCESS_RESULT) {
                    String address = resultData.getString(Constants.RESULT_DATA_KEY);
                    Log.e("geocoading","address in receiver : "+address);
                    mCollback.onSuccess(address);
                } else {
                    mCollback.onError("No Location Found");
                }
            }
        };

        Log.e("geocoading","lat: "+Double.toString(lat));
        Log.e("geocoading","long: "+Double.toString(lng));

        int fetchType = 2 ;
        String locationAddress = "";
        Intent intent = new Intent(mContext, GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER,mReceiver);
        intent.putExtra(Constants.FETCH_TYPE_EXTRA, fetchType);

        if(fetchType == Constants.USE_ADDRESS_NAME) {
            if(locationAddress.length() == 0) {
                Toast.makeText(mContext, "Please enter an address name", Toast.LENGTH_LONG).show();
                return;
            }
            intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA,locationAddress);
        }
        else {
            Location location = new Location("");
            location.setLatitude(lat);
            location.setLongitude(lng);
            intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        }
        //progressBar.setVisibility(View.VISIBLE);
        Log.e("requestLocationapi", "Starting Service");
        mContext.startService(intent);
    }
}
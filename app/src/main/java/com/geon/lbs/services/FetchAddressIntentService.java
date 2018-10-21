package com.geon.lbs.services;


import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.geon.lbs.helper.ConstantsFLA;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {
    ResultReceiver mReceiver;
    Bundle mBundle;
    public FetchAddressIntentService(){
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String errorMessage = "";
        List<Address> addresses = null;
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());


        mBundle=intent.getBundleExtra("bundleData");
        mReceiver=mBundle.getParcelable(ConstantsFLA.RECEIVER);
        LatLng latlng =mBundle.getParcelable(ConstantsFLA.LOCATION_DATA_EXTRA);

        //Log.e("intent service","started");
        //Log.e("Location ...", "Latitude = " + latlng.latitude + ", Longitude = " + latlng.longitude);

        try {
            addresses = geocoder.getFromLocation(latlng.latitude,latlng.longitude, 3);
        } catch (IOException ioException) {
            errorMessage = "service_not_available";
            Log.e("exception", errorMessage);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "invalid_lat_long_use";
            Log.e("exception","invalid_lat_long_use");
        }
        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "no_address_found";
               Log.e("errorMsg",errorMessage );
            }
            deliverResultToReceiver(ConstantsFLA.FAILURE_RESULT, errorMessage);
        } else {
            ArrayList<String> addressFragments = new ArrayList<String>();
            for(Address mAddress: addresses){
                Address address = mAddress;
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressFragments.add(address.getAddressLine(i));
                }
            }
            deliverResultToReceiver(ConstantsFLA.SUCCESS_RESULT, TextUtils.join(System.getProperty("line.separator"),
                                                                                                            addressFragments));
        }

    }
    private void deliverResultToReceiver(int resultCode, String message) {
        mBundle.putString(ConstantsFLA.RESULT_DATA_KEY, message);
        mReceiver.send(resultCode, mBundle);
        //Log.e("LocationSendToActicity","LocationSendToActicity");
    }

}

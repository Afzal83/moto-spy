package com.geon.lbs.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.geon.lbs.helper.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeocodeAddressIntentService extends IntentService {

    protected ResultReceiver resultReceiver;
    private static final String TAG = "geocoading";

    String errorMessage = "";

    public GeocodeAddressIntentService() {
        super("GeocodeAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.e("geocoading","service"+" started");

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        int fetchType = intent.getIntExtra(Constants.FETCH_TYPE_EXTRA, 0);
        if(fetchType == Constants.USE_ADDRESS_NAME) {
            Log.e("geocoading","fetch type address name");
            String name = intent.getStringExtra(Constants.LOCATION_NAME_DATA_EXTRA);
            try {
                addresses = geocoder.getFromLocationName(name, 1);
            } catch (IOException e) {
                errorMessage = "Service not available";
                Log.e(TAG, errorMessage, e);
            }
        }
        else if(fetchType == Constants.USE_ADDRESS_LOCATION) {

            Log.e("geocoading","fetch type address location");
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);

            try {
                addresses = geocoder.getFromLocation(
                        location.getLatitude(), location.getLongitude(), 5);
            } catch (IOException ioException) {
                errorMessage = "Service Not Available";
                Log.e(TAG, errorMessage, ioException);
            } catch (IllegalArgumentException illegalArgumentException) {
                errorMessage = "Invalid Latitude or Longitude Used";
                Log.e(TAG, errorMessage + ". " +
                        "Latitude = " + location.getLatitude() + ", Longitude = " +
                        location.getLongitude(), illegalArgumentException);
            }
        }
        else {
            errorMessage = "Unknown Type";
        }

        if (addresses == null || addresses.size()  == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "Not Found";
                Log.e("geocoading",""+errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage, null);
        } else {
           // Log.e("geocoading","address received");
            Address address = addresses.get(0);
 //           ArrayList<String> addressFragments = new ArrayList<>();
            String addressStr = addresses.get(0).getAddressLine(0);
//            Log.e("geocoading", "address : "+adr);

//            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
//                addressFragments.add(address.getAddressLine(i));
//                Log.e("geocoading", "address : "+address.getAddressLine(i));
//            }
            deliverResultToReceiver(Constants.SUCCESS_RESULT,addressStr, address);
        }
    }
    private void deliverResultToReceiver(int resultCode, String message, Address address) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RESULT_ADDRESS, address);
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        resultReceiver.send(resultCode, bundle);
    }
}

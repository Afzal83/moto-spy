package com.geon.spymoto.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;


import com.geon.spymoto.model.VehicleStatus;
import com.geon.spymoto.network.apputil.ApiUtils;
import com.geon.spymoto.network.remote.RetroService;

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Babu on 7/25/2017.
 */

public class DownLoadSingleVehicleStatusIntentService extends IntentService {

    String selectedVehicle = "";
    ResultReceiver rec  ;
    public DownLoadSingleVehicleStatusIntentService() {
        super("test-service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        selectedVehicle = intent.getStringExtra("selectedVehicle");
        rec = intent.getParcelableExtra("receiver");
        downLoadVehicelCurrentStatus();
    }
    void downLoadVehicelCurrentStatus(){
        RetroService mService = ApiUtils.getRetrofitService();
        mService.downLoadVehicleStatus(selectedVehicle)
                .enqueue(new retrofit2.Callback<VehicleStatus>() {

            @Override
            public void onResponse(Call<VehicleStatus> call, Response<VehicleStatus> response) {
                if(response.isSuccessful()) {
                    if(response.body().getError() == 0){

                        Bundle bundle = new Bundle();
                        bundle.putString("result ", "ok");
                        bundle.putParcelable("v_status", Parcels.wrap(response.body()));
                        rec.send(Activity.RESULT_OK, bundle);

                    }else{

                    }
                    //Log.e("regrofit","ok");
                }else {
                    int statusCode = response.code();
                    //Log.e("regrofit","error");
                }
                stopSelf();
            }
            @Override
            public void onFailure(Call<VehicleStatus> call, Throwable t) {
                //Log.e("retrofit","network error");
                stopSelf();
            }
        });
    }
}

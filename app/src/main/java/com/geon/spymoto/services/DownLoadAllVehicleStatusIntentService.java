package com.geon.spymoto.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.geon.spymoto.model.AllVehicleStatus;
import com.geon.spymoto.network.apputil.ApiUtils;
import com.geon.spymoto.network.remote.RetroService;

import org.parceler.Parcels;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Babu on 7/26/2017.
 */

public class DownLoadAllVehicleStatusIntentService extends IntentService {

    ResultReceiver rec;
    String imeiOfAllVehicle = "";

    public DownLoadAllVehicleStatusIntentService() {
        super("test-service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //Log.e("downloadIntentService","Started");
        rec = intent.getParcelableExtra("receiver");
        imeiOfAllVehicle = intent.getStringExtra("imeiOfAllVehicle");
        getAllVehicleCurrentStatus();
    }
    void getAllVehicleCurrentStatus(){
        RetroService mService = ApiUtils.getRetrofitService();
        mService.downLoadAllVehicleStatus(imeiOfAllVehicle).enqueue(new retrofit2.Callback<AllVehicleStatus>() {

            @Override
            public void onResponse(Call<AllVehicleStatus> call, Response<AllVehicleStatus> response) {
                if(response.isSuccessful()) {
                    if(response.body().getError() == 0){

                        Bundle bundle = new Bundle();
                        bundle.putString("result ", "ok");
                        bundle.putParcelable("vehicle_status_list", Parcels.wrap(response.body().getVehicles_status()));
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
            public void onFailure(Call<AllVehicleStatus> call, Throwable t) {
                //Log.e("retrofit","network error");
                stopSelf();
            }
        });
    }
}


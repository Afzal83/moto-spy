package com.geon.spymoto.serviceapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.geon.spymoto.AppGlobal;
import com.geon.spymoto.model.VehicleStatus;
import com.geon.spymoto.services.DownLoadSingleVehicleStatusIntentService;

import org.parceler.Parcels;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Babu on 3/20/2018.
 */

public class LiveTrackingApi {

    private Context mContext;
    private AppGlobal mGlobals;

    private Handler handler = new Handler();
    private Runnable runnableCodeToDownloadVehicleStatus;

    public LiveTrackingApi(Context mContext){
        this.mContext = mContext;
        mGlobals = ((AppGlobal)mContext.getApplicationContext());
    }

    public void getVehicleCurrentStatus(final Callback<VehicleStatus> callback){
        mGlobals.thread_for_livetracking_api=true;
        runnableCodeToDownloadVehicleStatus = new Runnable() {
            @Override
            public void run() {

                if(mContext==null){
                    return;
                }

                startDownLoadIntentService(callback);
                Log.e("Handlers", "Called on main thread................................");
                if(!mGlobals.thread_for_livetracking_api){
                    handler.removeCallbacks(runnableCodeToDownloadVehicleStatus);
                }else{
                    handler.postDelayed(this, 5000);
                }
            }
        };
        handler.post(runnableCodeToDownloadVehicleStatus);
    }
    private void startDownLoadIntentService(final Callback mCollback){
        ResultReceiver mReceiver=new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultCode == RESULT_OK) {
                    if(resultData == null){return;}
                    VehicleStatus vehicleStatus = Parcels.unwrap(resultData.getParcelable("v_status"));
                    if(null == vehicleStatus){
                        return;
                    }
                    mCollback.onSuccess(vehicleStatus);
                    Log.e("from vehicle status","successfull");
                }
            }
        };
        String vehicleImei = mGlobals.deviceVehiclePair.get(mGlobals.selectedVehicle);
        Intent i = new Intent(mContext,DownLoadSingleVehicleStatusIntentService.class);
        i.putExtra("selectedVehicle",vehicleImei);
        i.putExtra("receiver",mReceiver);
        mContext.startService(i);
    }
}

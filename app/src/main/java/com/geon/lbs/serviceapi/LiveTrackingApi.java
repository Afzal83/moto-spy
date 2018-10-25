package com.geon.lbs.serviceapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.geon.lbs.AppGlobal;
import com.geon.lbs.model.VehicleStatus;
import com.geon.lbs.services.DownLoadSingleVehicleStatusIntentService;

import org.parceler.Parcels;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Babu on 3/20/2018.
 */

public class LiveTrackingApi {

    private Context mContext;
    private AppGlobal mGlobals;
    private boolean isTrackingThereadRunnig = false;

    //private Handler handler = new Handler();
    private Runnable runnableCodeToDownloadVehicleStatus;

    private static Handler handlerSingleton = null;

    public LiveTrackingApi(Context mContext){
        this.mContext = mContext;
        mGlobals = ((AppGlobal)mContext.getApplicationContext());
    }

    private void createHandlerSingleton(){

        if(handlerSingleton == null){
            handlerSingleton = new Handler();
        }
    }

    public void getVehicleCurrentStatus(final Callback<VehicleStatus> callback){
        createHandlerSingleton();
        if(runnableCodeToDownloadVehicleStatus != null){
            handlerSingleton.removeCallbacks(runnableCodeToDownloadVehicleStatus);
        }
        runnableCodeToDownloadVehicleStatus = new Runnable() {
            @Override
            public void run() {

                if(mContext==null){
                    return;
                }
                startDownLoadIntentService(callback);
                //Log.e("Handlers", "Called on main thread from single vehicle download status");
                if(!mGlobals.thread_for_livetracking_api){
                    handlerSingleton.removeCallbacks(runnableCodeToDownloadVehicleStatus);
                }else{
                    handlerSingleton.postDelayed(this, 10000);
                }

            }
        };
        if(isTrackingThereadRunnig){
            handlerSingleton.postDelayed(runnableCodeToDownloadVehicleStatus, 10000);
        }
        else{
            handlerSingleton.post(runnableCodeToDownloadVehicleStatus);
        }

    }
    private void startDownLoadIntentService(final Callback mCallback){

        ResultReceiver mReceiver=new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultCode == RESULT_OK) {
                    if(resultData == null){
                        mCallback.onError("error");
                    }

                    VehicleStatus vehicleStatus = Parcels.unwrap(resultData.getParcelable("v_status"));
                    if(null == vehicleStatus){
                        mCallback.onError("error");
                    }
                    //Log.e("from Tracking result","successfull");
                    mCallback.onSuccess(vehicleStatus);


                }
                isTrackingThereadRunnig = false;
            }
        };
        final String vehicleImei = mGlobals.deviceVehiclePair.get(mGlobals.selectedVehicle);
        Intent i = new Intent(mContext,DownLoadSingleVehicleStatusIntentService.class);
        i.putExtra("selectedVehicle",vehicleImei);
        i.putExtra("receiver",mReceiver);
        mContext.startService(i);
        isTrackingThereadRunnig =true;
    }
}

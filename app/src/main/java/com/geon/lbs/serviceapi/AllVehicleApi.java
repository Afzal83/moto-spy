package com.geon.lbs.serviceapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.geon.lbs.AppGlobal;
import com.geon.lbs.model.VehicleStatus;
import com.geon.lbs.services.DownLoadAllVehicleStatusIntentService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Babu on 3/8/2018.
 */

public class AllVehicleApi {
    private Context mContext;
    private AppGlobal mGlobals;
    boolean isAllvehicleThereadRunnig = false ;

    private static Handler handlerForAllVehicleStatusDownload = null;
    private Runnable runnableCodeToDownLoadAllVehicleStatus;

    public AllVehicleApi(Context mContext){
        this.mContext = mContext;
        mGlobals = ((AppGlobal)mContext.getApplicationContext());
    }
    private void createHandlerSingleton(){

        if(handlerForAllVehicleStatusDownload == null){
            handlerForAllVehicleStatusDownload = new Handler();
        }
    }

    public void downLoadAllVehicleStatus(final Callback<List<VehicleStatus>> callback){
        createHandlerSingleton();
        if(runnableCodeToDownLoadAllVehicleStatus != null){
            handlerForAllVehicleStatusDownload.removeCallbacks(runnableCodeToDownLoadAllVehicleStatus);
        }
        runnableCodeToDownLoadAllVehicleStatus = new Runnable() {
            @Override
            public void run() {
                startDownLoadIntentService(callback);
                Log.e("Handlers", "Called on main thread from download all vehicle status");
                if(!mGlobals.thread_for_allvehicle_api){
                    handlerForAllVehicleStatusDownload.removeCallbacks(runnableCodeToDownLoadAllVehicleStatus);
                }else{
                    handlerForAllVehicleStatusDownload.postDelayed(this, 15000);
                }
            }
        };
        handlerForAllVehicleStatusDownload.post(runnableCodeToDownLoadAllVehicleStatus);

    }
    private String imeiCollectionOfAllVehicle(){
        String imeiOfAllVehicle = "";
        ArrayList<String> arList = new ArrayList<>();
        for (Map.Entry<String,String> entry : mGlobals.deviceVehiclePair.entrySet()) {
            arList.add(entry.getValue());
        }
        if(!arList.isEmpty()) {
            //Log.e("allImei","baaaaaaaaaaaaaal");
            for (int i = 0; i < arList.size(); i++) {
                if (i == arList.size() - 1) {
                    imeiOfAllVehicle += arList.get(i);
                } else {
                    imeiOfAllVehicle += arList.get(i) + "|";
                }
            }
            //Log.e("allImei", "All Imei:" + imeiOfAllVehicle);
        }
        return imeiOfAllVehicle;
    }

    private void startDownLoadIntentService(final Callback<List<VehicleStatus>> callback){
        ResultReceiver mReceiver=new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);

                isAllvehicleThereadRunnig = false;

                if (resultCode == RESULT_OK) {
                    //Log.e("result from download","successfull");
                    ArrayList<VehicleStatus> mList = Parcels.unwrap(resultData.getParcelable("vehicle_status_list"));
                    if(null == mList){
                        //Log.e("service_return","got null allvehicle list");
                    }else{
                        callback.onSuccess(mList);
                    }
                }

            }
        };

        Intent i = new Intent(mContext,DownLoadAllVehicleStatusIntentService.class);
        i.putExtra("receiver",mReceiver);
        i.putExtra("imeiOfAllVehicle",imeiCollectionOfAllVehicle());
        mContext.startService(i);
        isAllvehicleThereadRunnig = true;
    }
}

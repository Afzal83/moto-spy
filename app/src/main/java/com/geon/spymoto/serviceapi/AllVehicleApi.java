package com.geon.spymoto.serviceapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;

import com.geon.spymoto.AppGlobal;
import com.geon.spymoto.model.VehicleStatus;
import com.geon.spymoto.services.DownLoadAllVehicleStatusIntentService;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Babu on 3/8/2018.
 */

public class AllVehicleApi {
    Context mContext;
    AppGlobal mGlobals;

    private Handler handler = new Handler();
    private Runnable runnableCodeToDownLoadAllVehicleStatus;

    public AllVehicleApi(Context mContext){
        this.mContext = mContext;
        mGlobals = ((AppGlobal)mContext.getApplicationContext());
    }

    public void downLoadAllVehicleStatus(final Callback<List<VehicleStatus>> callback){
        mGlobals.thread_for_allvehicle_api=true;
        runnableCodeToDownLoadAllVehicleStatus = new Runnable() {
            @Override
            public void run() {
                startDownLoadIntentService(callback);
                Log.e("Handlers", "Called on main thread................................");
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                if(!mGlobals.thread_for_allvehicle_api){
                    handler.removeCallbacks(runnableCodeToDownLoadAllVehicleStatus);
                }else{
                    handler.postDelayed(this, 10000);
                }
            }
        };
        handler.post(runnableCodeToDownLoadAllVehicleStatus);
    }
    private String imeiCollectionOfAllVehicle(){
        String imeiOfAllVehicle = "";
        ArrayList<String> arList = new ArrayList<String>();
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
            Log.e("allImei", "All Imei:" + imeiOfAllVehicle);
        }
        return imeiOfAllVehicle;
    }

    private void startDownLoadIntentService(final Callback<List<VehicleStatus>> callback){
        ResultReceiver mReceiver=new ResultReceiver(new Handler()){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                super.onReceiveResult(resultCode, resultData);
                if (resultCode == RESULT_OK) {
                    //Log.e("result from download","successfull");
                    ArrayList<VehicleStatus> mList = Parcels.unwrap(resultData.getParcelable("vehicle_status_list"));
                    if(null == mList){
                        Log.e("service_return","got null allvehicle list");
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
    }
}

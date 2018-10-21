package com.geon.lbs.serviceapi;

import android.util.Log;

import com.geon.lbs.helper.Constants;
import com.geon.lbs.model.Vehicle;
import com.geon.lbs.model.AllVehicleList;
import com.geon.lbs.network.apputil.ApiUtils;
import com.geon.lbs.network.remote.RetroService;

import java.util.List;


import retrofit2.Call;
import retrofit2.Response;


public class VehicleApi {




    public void getAllVehicleList(String userName, String userPass, String userCategory
            ,final Callback <List<Vehicle>> callback){

        Log.e("allVehicleApiCall0 : ",""+userName);
        Log.e("allVehicleApiCall1 : ",""+userPass);
        Log.e("allVehicleApiCall2 : ",""+userCategory);

        RetroService mService = ApiUtils.getRetrofitService();
        mService.downLoadUserAllVehicle(
                userName
                ,userPass
                ,userCategory
        ).enqueue(new retrofit2.Callback<AllVehicleList>() {
            @Override
            public void onResponse(Call<AllVehicleList> call, Response<AllVehicleList> response) {
                if(response.isSuccessful()) {
                    if(response.body().getError() == 1){
                        Log.e("allVehicleList: ",response.body().getError()+"");
                        callback.onError(response.body().getMessage());
                    }else{
                        Log.e("allVehicleList: ","success");
                        callback.onSuccess(response.body().getVehicle_list());
                    }
                }else {
                    int statusCode = response.code();
                    Log.e("allVehicleList:Code:: ",statusCode+"");
                    callback.onError(Constants.ERROR);
                }
            }
            @Override
            public void onFailure(Call<AllVehicleList> call, Throwable t) {
                Log.e("allVehicleList: ::",Constants.NETWORK_ERROR);
                callback.onError(Constants.NETWORK_ERROR);
            }
        });
    }

}

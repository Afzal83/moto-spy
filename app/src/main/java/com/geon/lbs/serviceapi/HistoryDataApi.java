package com.geon.lbs.serviceapi;

import android.util.Log;

import com.geon.lbs.model.HistoryData;
import com.geon.lbs.model.LocationData;
import com.geon.lbs.network.apputil.ApiUtils;
import com.geon.lbs.network.remote.RetroService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Babu on 3/22/2018.
 */

public class HistoryDataApi {

    public void downLoadHistoryData(String imei, String startTime, String endTime, final Callback<List<LocationData>> callback){


        RetroService mService = ApiUtils.getRetrofitService();
        mService.downloadHistoryData(imei,startTime,endTime).enqueue(new retrofit2.Callback<HistoryData>() {

            @Override
            public void onResponse(Call<HistoryData> call, Response<HistoryData> response) {
                if(response.isSuccessful()) {
                    if(response.body().getError() == 0){
                        callback.onSuccess(response.body().getRecord_list());
                    }else{
                        callback.onError("Error");
                    }
                }else {
                    int statusCode = response.code();
                    callback.onError("Error");
                }
            }
            @Override
            public void onFailure(Call<HistoryData> call, Throwable t) {
                callback.onError("Error");
                Log.e("retrofit","network error");
            }
        });
    }
}

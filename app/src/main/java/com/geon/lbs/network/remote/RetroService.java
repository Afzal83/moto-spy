package com.geon.lbs.network.remote;


import com.geon.lbs.model.AllVehicleStatus;
import com.geon.lbs.model.HistoryData;
import com.geon.lbs.model.User;
import com.geon.lbs.model.AllVehicleList;
import com.geon.lbs.model.VehicleStatus;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Babu on 1/5/2018.
 */

public interface RetroService {

    @POST("api/api_auth")
    @FormUrlEncoded
    Call<User> userLogin(@Field("user_name") String user_name
                        ,@Field("user_password") String user_password);

    @POST("api/vehicle_list")
    @FormUrlEncoded
    Call<AllVehicleList> downLoadUserAllVehicle(@Field("user_name") String user_name
                                            ,@Field("user_password") String user_password
                                            ,@Field("user_type") String user_type);


    @POST("api/all_vehicle_current_status")
    @FormUrlEncoded
    Call<AllVehicleStatus> downLoadAllVehicleStatus(@Field("emei_number_combine") String emei_number_combine);

    @POST("api/vehicle_current_status")
    @FormUrlEncoded
    Call<VehicleStatus> downLoadVehicleStatus(@Field("emei_number") String emei_number);

    @POST("api/driving_path")
    @FormUrlEncoded
    Call<HistoryData> downloadHistoryData(@Field("emei_number") String emei_number
            , @Field("start_time") String start_time
            , @Field("end_time") String end_time);
}

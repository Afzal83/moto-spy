package com.geon.spymoto.helper;

import android.util.Log;

/**
 * Created by Babu on 9/29/2016.
 */
public class RestImplimentationMethod {
    String baseUrl =  "http://www.geon-bd.com/vts/index.php/api";
    public String urlForLogin = baseUrl+"/api_auth";
    public String urlForUserVehicleList = baseUrl+"/vehicle_list";
    public String urlForCurrentLocation = baseUrl+"/vehicle_current_status";
    public String urlForDrivingPath = baseUrl+"/driving_path";
    public String urlForAllVehicleStatus = baseUrl+"/all_vehicle_current_status";


    public String createLoginParam(String username, String password){
        return "user_name="+username+"&"+"user_password="+password;
    }
    public String downloadUserVehiclesParam(String username, String password, String userType){
        return "user_name="+username+"&"+"user_password="+password+"&"+"user_type="+userType;
    }
    public String createCurrentLocationDownLoadParam(String Vehicleimei){
        return "emei_number="+Vehicleimei ;
    }
    public String createHistoryDataUrlParams(String imei, String startTime, String endTime){
        String s = "emei_number="+imei+"&"+"start_time="+startTime+"&"+"end_time="+endTime ;
        Log.e("postParam",s);
        return s;
    }
    public String createAllVehicleCurrentStatusParam(String allVehiclesImei){
        String s = "emei_number_combine="+allVehiclesImei;
        return s;
    }







    public String createDownLoadAllAccountParam(String userName, String password){
        return "identity="+userName+"&"+"password="+password;
    }

    public String createAllVehiclesUrlParam(String userName, String password, String accountName){
        return "identity="+userName+"&"+"password="+password+"&"+"account_name="+accountName;
    }


    /*   /api/mobile/history
    params:identity,password,vehicle_reg,start,end
    start,end datetime format: YYYY-MM-DD HH:mm:ss */
}

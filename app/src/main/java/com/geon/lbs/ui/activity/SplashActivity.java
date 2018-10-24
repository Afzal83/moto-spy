package com.geon.lbs.ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.geon.lbs.AppGlobal;
import com.geon.lbs.R;
import com.geon.lbs.model.User;
import com.geon.lbs.model.Vehicle;
import com.geon.lbs.serviceapi.Callback;
import com.geon.lbs.serviceapi.UserApi;
import com.geon.lbs.serviceapi.VehicleApi;

import java.util.List;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private final static String TAG = "SplashActivity";

    UserApi userApi;
    VehicleApi vehicleApi;
    AppGlobal appGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.e(TAG,"onCreate");

        appGlobal =((AppGlobal)this.getApplicationContext());
        userApi = new UserApi(this);
        vehicleApi = new VehicleApi();

        getUserDataFromSp();

        downLoadUserAllVehicle();
    }
    void downLoadUserAllVehicle(){

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading Data ...");
        dialog.setCancelable(false);
        dialog.show();

        vehicleApi.getAllVehicleList(appGlobal.userName, appGlobal.userPassword, appGlobal.userCatagory
                , new Callback<List<Vehicle>>() {
                    @Override
                    public void onSuccess(List<Vehicle> response) {
                        dialog.dismiss();
                        appGlobal.deviceVehiclePair.clear();
                        appGlobal.geonVehicleList.clear();
                        appGlobal.geonVehicleList.addAll(response);

                        if(appGlobal.geonVehicleList.size()==1){
                            appGlobal.selectedVehicle = appGlobal.geonVehicleList.get(0).getNumber_plate();
                        }
                        for(Vehicle  myVehicle : appGlobal.geonVehicleList){

//                            Log.e(">>vehicle info ","id:"+ myVehicle.getVehicle_id());
//                            Log.e(">>vehicle info ","imei:"+ myVehicle.getEmei_number());
//                            Log.e(">>vehicle info ","plate"+ myVehicle.getNumber_plate());
//                            Log.e(">>vehicle info ","load status:"+ myVehicle.getIs_loaded());
//                            Log.e(">>vehicle info ","callbacksim:"+ myVehicle.getCall_back_sim());
//                            Log.e(">>vehicle info ","speedlimit:"+ myVehicle.getSpeed_limit());
//                            Log.e(">>vehicle info ","vekhiclecode:"+ myVehicle.getVehicle_code());
                            if(myVehicle.getEmei_number() == null){continue;}
                            appGlobal.deviceVehiclePair.put(myVehicle.getNumber_plate(),myVehicle.getEmei_number());
                        }
//                        for (Map.Entry<String,String> entry : appGlobal.deviceVehiclePair.entrySet()) {
//                            String imeiToCheck = entry.getValue();
//                            String numberPlate = entry.getKey();
//                            Log.e("debug->","imei:"+imeiToCheck+ "     "+"numbrPlate: "+numberPlate);
//                        }
                        startActivity(new Intent(SplashActivity.this,NavActivity.class));
                        SplashActivity.this.finish();
                    }

                    @Override
                    public void onError(String s) {
                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                        builder.setTitle("This is list choice dialog box")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        SplashActivity.this.finish();
                                        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
                                    }
                                });
                        builder.create();
                    }
                });
    }
    void getUserDataFromSp(){
        User user = userApi.getUserDataFromSharedPreference();
        appGlobal.isUserLogedin = user.isUserLoogedIn();
        appGlobal.userName = user.getUser_name();
        appGlobal.userPassword = user.getUserPassword();
        appGlobal.userEmail = user.getEmail();
        appGlobal.userCatagory = user.getUser_type();
        appGlobal.userFirsName = user.getName_first();
        appGlobal.userLastName = user.getName_last();
        appGlobal.userPhone = user.getContact_mobile();

//        Log.e(TAG,"user_name:"+appGlobal.userName);
//        Log.e(TAG,"user_first_name:"+appGlobal.userFirsName);
//        Log.e(TAG,"user_last_name:"+appGlobal.userLastName);
//        Log.e(TAG,"user_type:"+ appGlobal.userCatagory);
//        Log.e(TAG,"user_pass:"+appGlobal.userPassword);
//        Log.e(TAG,"user_userEmail:"+appGlobal.userEmail);
//        Log.e(TAG,"user_phone:"+appGlobal.userPhone);
    }
}

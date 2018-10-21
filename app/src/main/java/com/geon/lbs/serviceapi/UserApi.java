package com.geon.lbs.serviceapi;

import android.content.Context;
import android.util.Log;

import com.geon.lbs.helper.Constants;
import com.geon.lbs.model.User;
import com.geon.lbs.network.apputil.ApiUtils;
import com.geon.lbs.network.remote.RetroService;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Babu on 3/29/2018.
 */

public class UserApi {

    Context mContext;
    public  UserApi(Context mContext){
        this.mContext = mContext;
    }

    public  void saveUserInfoToSharedPreference(User user){
        SPapi spApi = new SPapi(mContext);
        spApi.saveBoolToSharedPreference(Constants.USER_IS_LOGGEDIN,user.isUserLoogedIn());
        spApi.saveStrngToSharedPreference(Constants.USER_NAME,user.getUser_name());
        spApi.saveStrngToSharedPreference(Constants.USER_PASSWORD,user.getUserPassword());
        spApi.saveStrngToSharedPreference(Constants.USER_EMAIL,user.getEmail());
        spApi.saveStrngToSharedPreference(Constants.USER_CATAGORY,user.getUser_type());
        spApi.saveStrngToSharedPreference(Constants.USER_FIRST_NAME,user.getName_first());
        spApi.saveStrngToSharedPreference(Constants.USER_LAST_NAME,user.getName_last());
        //spApi.saveStrngToSharedPreference(Constants.USER_ADDRESS_LINE_ONE,user.getAddress());
        //spApi.saveStrngToSharedPreference(Constants.USER_ADDRESS_LINE_ONE,user.getAddress());
        spApi.saveStrngToSharedPreference(Constants.USER_PHONE,user.getContact_mobile());
        //spApi.saveStrngToSharedPreference(Constants.USER_POST_CODE,user.getUserPostCode());
        //spApi.saveStrngToSharedPreference(Constants.USER_CITY,user.getUserCity());
        //spApi.saveStrngToSharedPreference(Constants.USER_IMAGE,user.getUserImage());
    }
    public User getUserDataFromSharedPreference(){
        User user = new User();
        SPapi spApi = new SPapi(mContext);

        user.setUserLoogedIn(spApi.getBoolFromSharedPreference(Constants.USER_IS_LOGGEDIN));
        user.setUser_name(spApi.getStringFromSharedPreference(Constants.USER_NAME));
        user.setUserPassword(spApi.getStringFromSharedPreference(Constants.USER_PASSWORD));
        user.setUser_type(spApi.getStringFromSharedPreference(Constants.USER_CATAGORY));
        user.setEmail(spApi.getStringFromSharedPreference(Constants.USER_EMAIL));
        user.setName_first(spApi.getStringFromSharedPreference(Constants.USER_FIRST_NAME));
        user.setName_last(spApi.getStringFromSharedPreference(Constants.USER_LAST_NAME));
        user.setContact_mobile(spApi.getStringFromSharedPreference(Constants.USER_PHONE));

        return user;
    }

    public void doLogin(String userName,String password,final Callback<User> callback){

        RetroService mService = ApiUtils.getRetrofitService();
        mService.userLogin(userName,password).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()) {
                    if(response.body().getError() == 1){
                        callback.onError(response.body().getMessage());
                    }else{
                        Log.e("login:user: ","problem here");
                        Log.e("login:user: ",response.body().getName_first());
                        Log.e("login:user: ",response.body().getName_last());
                        callback.onSuccess(response.body());
                    }
                }else {
                    callback.onError(Constants.ERROR);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(Constants.NETWORK_ERROR);
            }
        });
    }
}

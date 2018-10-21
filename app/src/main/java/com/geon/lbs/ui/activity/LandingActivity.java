package com.geon.lbs.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.geon.lbs.AppGlobal;
import com.geon.lbs.R;
import com.geon.lbs.model.User;
import com.geon.lbs.serviceapi.UserApi;
import com.geon.lbs.serviceapi.Callback;

public class LandingActivity extends AppCompatActivity {

    AppGlobal appGlobal;
    UserApi userApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("screen density===>>", "" + getResources().getDisplayMetrics().density);

        setContentView(R.layout.activity_landing);

        appGlobal = ((AppGlobal) getApplicationContext());
        userApi = new UserApi(this);

        if(!isOnline()){
            new AlertDialog.Builder(this)
                    .setTitle(LandingActivity.this.getString(R.string.network_error))
                    .setMessage(LandingActivity.this.getString(R.string.check_your_internet))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 110);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }else{
            makeDecision();
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
    void makeDecision(){

        User user = userApi.getUserDataFromSharedPreference();

        Log.e("user Info:","userName: "+appGlobal.userName+"userPass: "+appGlobal.userPassword);

        if (user.isUserLoogedIn()) {
            Log.e("user loggin status===","......already logged in");
            checkUserOauth(user.getUser_name(),user.getUserPassword());
        } else {
            Log.e("user loggin status===","......not logged in");
            startActivity(new Intent(this,LoginActivity.class));
            this.finish();
        }
    }
    void checkUserOauth(String userName, final String userPassword){
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
            .title(getResources().getString(R.string.loading))
            .content(getResources().getString(R.string.please_wait))
            .progress(true, 0)
            .show();
        userApi.doLogin(userName, userPassword, new Callback<User>() {
            @Override
            public void onSuccess(User response) {
                dialog.dismiss();
                startActivity(new Intent(LandingActivity.this,SplashActivity.class));
                response.setUserLoogedIn(true);
                response.setUserPassword(userPassword);
                userApi.saveUserInfoToSharedPreference(response);
                LandingActivity.this.finish();
            }
            @Override
            public void onError(String s) {
                dialog.dismiss();
                startActivity(new Intent(LandingActivity.this,LoginActivity.class));
                LandingActivity.this.finish();
            }
        });

    }
}

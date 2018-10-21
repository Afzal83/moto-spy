package com.geon.lbs.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.geon.lbs.R;
import com.geon.lbs.model.User;
import com.geon.lbs.serviceapi.UserApi;
import com.geon.lbs.serviceapi.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    UserApi userApi;

    EditText userName_et,userPass_et;
    Button loginBtn,contactUsBtn;

    String userName = "";
    String userPass = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userApi = new UserApi(this);

        bindView();
    }
    void bindView(){
        userName_et = (EditText)findViewById(R.id.user_name);
        userPass_et= (EditText)findViewById(R.id.user_pass);

        loginBtn = (Button)findViewById(R.id.login);
        contactUsBtn =(Button)findViewById(R.id.contact_us);

        loginBtn.setOnClickListener(this);
        contactUsBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login:
                doLoginAction();
                break;
            case R.id.contact_us:
                contactUs();
                break;
        }
    }
    void doLoginAction(){
        userName = userName_et.getText().toString();
        userPass = userPass_et.getText().toString();
        if(userName.isEmpty()){
            return ;
        }
        if(userPass.isEmpty()){
            return;
        }
        final ProgressDialog dialog = new ProgressDialog(this, R.style.StyledDialog);
        dialog.setMessage("Loggin in...");
        dialog.setCancelable(false);
        dialog.show();

        userApi.doLogin(userName, userPass, new Callback<User>() {
            @Override
            public void onSuccess(User response) {
                dialog.dismiss();
                Log.e("login_response:","userFirstName"+response.getName_first());
                response.setUserLoogedIn(true);
                response.setUserPassword(userPass);
                userApi.saveUserInfoToSharedPreference(response);
                startActivity(new Intent(LoginActivity.this,SplashActivity.class));
                LoginActivity.this.finish();
            }

            @Override
            public void onError(String s) {
                dialog.dismiss();
                Log.e("login_response:","login Error");
            }
        });
    }
    void contactUs(){
        startActivity(new Intent(LoginActivity.this,ContactUsActivity.class));
    }
}

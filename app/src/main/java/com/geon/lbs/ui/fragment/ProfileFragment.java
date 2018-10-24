package com.geon.lbs.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geon.lbs.AppGlobal;
import com.geon.lbs.R;

public class ProfileFragment extends Fragment {

    View mView;
    TextView userName,userPhone,userEmail,totalVehicle,totalActiveVehicle,totalInactiveVehicle;
    AppGlobal appGlobal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_profile,container,false);
        appGlobal =((AppGlobal)getActivity().getApplicationContext());
        bindView();
        Toolbar toolbar = (Toolbar)mView.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Profile");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        return mView;
    }
    void bindView(){
        userName = mView.findViewById(R.id.user_name);
        userPhone = mView.findViewById(R.id.user_phone);
        userEmail = mView.findViewById(R.id.user_email);
        totalVehicle = mView.findViewById(R.id.no_of_total_vehicle);
        totalActiveVehicle = mView.findViewById(R.id.no_of_active_vehicle);
        totalInactiveVehicle = mView.findViewById(R.id.no_of_inactive_vehicle);

        String userNameStr = appGlobal.userFirsName + " "+appGlobal.userLastName;
        String userPhoneStr = appGlobal.userPhone;
        String userEmailStr = appGlobal.userEmail;

        userName.setText(userNameStr);
        userPhone.setText(userPhoneStr);
        userEmail.setText(userEmailStr);
        totalVehicle.setText(appGlobal.geonVehicleList.size());
        totalActiveVehicle.setText(appGlobal.geonVehicleList.size());
        totalInactiveVehicle.setText("0");
    }
}

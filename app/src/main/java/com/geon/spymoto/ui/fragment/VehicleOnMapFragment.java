package com.geon.spymoto.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.geon.spymoto.AppGlobal;
import com.geon.spymoto.R;
import com.geon.spymoto.model.LocationData;
import com.geon.spymoto.model.NearestLocation;
import com.geon.spymoto.model.VehicleStatus;
import com.geon.spymoto.serviceapi.AllVehicleApi;
import com.geon.spymoto.serviceapi.Callback;
import com.geon.spymoto.serviceapi.HistoryDataApi;
import com.geon.spymoto.serviceapi.LiveTrackingApi;
import com.geon.spymoto.ui.activity.DateTimeActivity;
import com.geon.spymoto.ui.activity.TrackingDuration;
import com.geon.spymoto.ui.activity.VehicleSearchActivity;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.geon.spymoto.ui.fragment.BaseFragment.OPMode.HISTORY;
import static com.geon.spymoto.ui.fragment.BaseFragment.OPMode.HOME;
import static com.geon.spymoto.ui.fragment.BaseFragment.OPMode.TRACKING;


/**
 * Created by Babu on 3/7/2018.
 */

public class VehicleOnMapFragment extends MapFragment implements View.OnClickListener{

    private final String TAG = "VehicleOnMapFragment";



    Button home,startTracking,showHistory,clearMap,selectVehicle,selectTime;
    LinearLayout selectedVehicleContainer,selectDurationContainer,speedContainer_m;

    ScrollView nearByLocationContainer;
    TextView selectedVehicle,startTime,endTime ;
    TextView nearestLocationOne,nearestLocationTwo,nearestLocationThree,nearestLocationFour,nearestLocationFive;


    AllVehicleApi vehicleApi;
    LiveTrackingApi liveTrackingApi;
    HistoryDataApi historyDataApi;

    List<VehicleStatus> allVehicleStatusList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.map_fragment,container,false);

        mGlobals = ((AppGlobal)getActivity().getApplicationContext());
        vehicleApi = new AllVehicleApi(getActivity());
        liveTrackingApi = new LiveTrackingApi(getActivity());
        historyDataApi = new HistoryDataApi();

        buildGoogleApiClient();
        initView();
        if (checkPlayServices()) {
            Log.e("=====google api======","ase google api");
            buildGoogleApiClient();
        }
        Log.e(TAG,"onCreateView");
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("calling ..","onResume");
        setSelectedVehicle();
        startHomeOpearation();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.track_all_vehicle:
                startHomeOpearation();
                break;
            case R.id.start_tracking:
                startTrackingOpearation();
                break;

            case R.id.show_history:
                startHistoryOpearation();
                break;

            case R.id.clear_map:
                mMap.clear();
                break;

            case R.id.select_vehicle:
                mMap.clear();
                Intent i = new Intent(getActivity(),VehicleSearchActivity.class);
                startActivityForResult(i, 1);
                break;

            case R.id.select_history_time:
                //mMap.clear();
                Intent mIntent = new Intent(getActivity(),DateTimeActivity.class);
                startActivityForResult(mIntent, 2);
                break;
        }
        setHeaderVisibility();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                setSelectedVehicle();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        else if (requestCode == 2) {
            if(resultCode == Activity.RESULT_OK){
                setSelectedDuration();
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    void initView(){

        allVehicleStatusList = new ArrayList<VehicleStatus>();

        selectedVehicleContainer  = (LinearLayout)mView.findViewById(R.id.selected_vehicle_container);
        selectDurationContainer = (LinearLayout)mView.findViewById(R.id.duration_container);
        nearByLocationContainer = (ScrollView)mView.findViewById(R.id.nearby_location_container_lienear_l);
        speedContainer_m = (LinearLayout)mView.findViewById(R.id.speed_container_m);
        speedContainer_c = (LinearLayout)mView.findViewById(R.id.speed_container_c);

        selectedVehicle = (TextView)mView.findViewById(R.id.selected_vehicle);
        startTime = (TextView)mView.findViewById(R.id.start_time);
        endTime = (TextView)mView.findViewById(R.id.end_time);
        vehicleSpeed_tv = (TextView)mView.findViewById(R.id.vehicle_speed) ;

        nearestLocationOne = (TextView)mView.findViewById(R.id.address_line_one);
        nearestLocationTwo = (TextView)mView.findViewById(R.id.address_line_two);
        nearestLocationThree = (TextView)mView.findViewById(R.id.address_line_three);
        nearestLocationFour = (TextView)mView.findViewById(R.id.address_line_four);
        nearestLocationFive = (TextView)mView.findViewById(R.id.address_line_five);


        clearMap = (Button)mView.findViewById(R.id.clear_map);
        showHistory = (Button)mView.findViewById(R.id.show_history);
        startTracking = (Button)mView.findViewById(R.id.start_tracking);
        home = (Button)mView.findViewById(R.id.track_all_vehicle);


        selectVehicle =  (Button)mView.findViewById(R.id.select_vehicle);
        selectTime =  (Button)mView.findViewById(R.id.select_history_time);

        clearMap.setOnClickListener(this);
        home.setOnClickListener(this);
        startTracking.setOnClickListener(this);
        showHistory.setOnClickListener(this);

        selectVehicle.setOnClickListener(this);
        selectTime.setOnClickListener(this);

    }
    @Override
    void setSelectedVehicle(){
        if(mGlobals.selectedVehicle.isEmpty()){
            String str = getString(R.string.select_a_vehicle);
            selectedVehicle.setText(str);
        }else{
            selectedVehicle.setText(mGlobals.selectedVehicle);
        }

    }
    void setSelectedDuration(){
        startTime.setText(mGlobals.startTime);
        endTime.setText(mGlobals.endTime);
    }

    @Override
    void setHeaderVisibility(){
        selectDurationContainer.setVisibility(View.GONE);
        nearByLocationContainer.setVisibility(View.GONE);
        speedContainer_m.setVisibility(View.GONE);
        switch (OpearationMode){
            case HOME:
                break;
            case TRACKING:
                speedContainer_m.setVisibility(View.VISIBLE);
                break;
            case HISTORY:
                selectDurationContainer.setVisibility(View.VISIBLE);
                setSelectedDuration();
                break;
            default:
                break;

        }
    }
    @Override
    void stopAllBackgroundService(){
        mGlobals.thread_for_allvehicle_api=false;
        mGlobals.thread_for_livetracking_api=false;
    }

    @Override
    void showNearbyLocation(List<NearestLocation> nearestLocatoinList){
        nearByLocationContainer.setVisibility(View.VISIBLE);
        for (int i = 0; i < nearestLocatoinList.size(); i++) {
            String mResult = String.format("%.2f", nearestLocatoinList.get(i).distance);
            if (i == 0) {
                nearestLocationOne.setText(mResult + " Km way from " + nearestLocatoinList.get(i).PlaceName);
            } else if (i == 1) {
                nearestLocationTwo.setText(mResult + " Km way from " + nearestLocatoinList.get(i).PlaceName);
            } else if (i == 2) {
                nearestLocationThree.setText(mResult + " Km way from " + nearestLocatoinList.get(i).PlaceName);
            } else if (i == 3) {
                nearestLocationFour.setText(mResult + " Km way from " + nearestLocatoinList.get(i).PlaceName);
            } else if (i == 4) {
                nearestLocationFive.setText(mResult + " Km way from " + nearestLocatoinList.get(i).PlaceName);
            }
        }
    }

    /*Home opearation is from here*/
    @Override
    void startHomeOpearation(){
        stopAllBackgroundService();
        if(null != mMap){
            mMap.clear();
        }
        OpearationMode = HOME;
        setHeaderVisibility();

        Log.e("calling ..","All vehicle status");
        vehicleApi.downLoadAllVehicleStatus(new Callback<List<VehicleStatus>>() {
            @Override
            public void onSuccess(List<VehicleStatus> response) {
                Log.e("result from download","successfull");

                if(isFirstDataToPlot){
                    isFirstDataToPlot = false;
                    return;
                }
                plotUsersAllVehicl(response);
                Log.e(TAG,"SUCCESS");
            }
            @Override
            public void onError(String s) {
                Log.e(TAG,"ERROR");
            }
        });
    }
    void plotUsersAllVehicl(List<VehicleStatus> usersAllVehicleStatusList){
        if(!mGlobals.thread_for_allvehicle_api){return;}
        for(int i=0; i<usersAllVehicleStatusList.size(); i++){
            for (Map.Entry<String,String> entry : mGlobals.deviceVehiclePair.entrySet()) {
                String imeiToCheck = entry.getValue();
                //Log.e("imei :::","### ::: "+imeiToCheck);
                //Log.e("returned imei :::","retImei:"+usersAllVehicleStatusList.get(i).getDevice_emei());
                if(imeiToCheck.contentEquals(usersAllVehicleStatusList.get(i).getDevice_emei())){
                    usersAllVehicleStatusList.get(i).setNumberPlate(entry.getKey());
                    break;
                }
            }
            plotDataOnMap(usersAllVehicleStatusList.get(i));
        }
        setSelectedVehicle();
    }


    /*Live Tracking functionally from here */
    @Override
    void startTrackingOpearation(){
        if(mGlobals.selectedVehicle.contentEquals("")){
            Toast.makeText(getActivity(), "Please Select a Vehicle : / ",
                    Toast.LENGTH_LONG).show();
            return;
        }
        stopAllBackgroundService();
        OpearationMode = TRACKING ;
        setHeaderVisibility();
        setSelectedVehicle();
        isFirstDataToPlot = true;
        pLat=0.0;
        pLong = 0.0;
        mMap.clear();
        liveTrackingApi.getVehicleCurrentStatus(new Callback<VehicleStatus>() {
            @Override
            public void onSuccess(VehicleStatus response) {
                if(!mGlobals.thread_for_livetracking_api){
                    return;
                }
                Log.e("back ",response.getDevice_emei());
                Log.e("back ",response.getRecord_time());
                LocationData locationData = new LocationData();
                locationData.setLatitude(response.getLatitude());
                locationData.setLongitude(response.getLongitude());
                locationData.setSpeed(response.getSpeed());
                locationData.setData_status(response.getData_status());
                locationData.setEngine_status(response.getEngine_status());
                locationData.setAc_status(response.getAc_status());
                locationData.setRecord_date(response.getRecord_time());
                setCurrentPosition(locationData);
            }

            @Override
            public void onError(String s) {

            }
        });
    }

    /*history opearation start from here*/
    @Override
    void startHistoryOpearation(){

        if(mGlobals.selectedVehicle.contentEquals("")){
            Toast.makeText(getActivity(), "Please Select a Vehicle : / ",
                    Toast.LENGTH_LONG).show();
            return;
        }
        String vehicleImei = mGlobals.deviceVehiclePair.get(mGlobals.selectedVehicle);
        stopAllBackgroundService();

        if(null != mMap){
            mMap.clear();
        }
        OpearationMode = HISTORY;
        setHeaderVisibility();


        historyDataApi.downLoadHistoryData(vehicleImei, mGlobals.startTime, mGlobals.endTime
                , new Callback<List<LocationData>>() {
                    @Override
                    public void onSuccess(List<LocationData> response) {
                        Log.e(TAG,"SUCCESS");
                        Log.e(TAG,response.size()+"");

                       // showHistoryBtn.setEnabled(false);
                        if(response.size()>2){
                            //distanceTraveled = MapHelper.calculateDistance(response);
                        }
                        for(int i=0; i<response.size(); i++){
                            //Log.e("lat",response.get(i).getLatitude())
                            plotHistoryData(response,response.get(i),i);
                        }
                       // travelledDistanceTv.setText(distanceTraveled+" Km");
                       // distanceTravelledContainer.setVisibility(View.VISIBLE);
                       // showHistoryBtn.setEnabled(true);

                    }
                    @Override
                    public void onError(String s) {
                        Log.e(TAG,"eRRor");
                    }
                });
    }


    @Override
    public void onMapClick(LatLng latLng) {
        super.onMapClick(latLng);
        nearByLocationContainer.setVisibility(View.GONE);
    }
}

package com.geon.lbs.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geon.lbs.AppGlobal;
import com.geon.lbs.R;
import com.geon.lbs.helper.MapHelper;
import com.geon.lbs.model.LocationData;
import com.geon.lbs.model.VehicleStatus;
import com.geon.lbs.serviceapi.AllVehicleApi;
import com.geon.lbs.serviceapi.Callback;
import com.geon.lbs.serviceapi.HistoryDataApi;
import com.geon.lbs.serviceapi.LiveTrackingApi;
import com.geon.lbs.serviceapi.RequestLocationAddressApi;
import com.geon.lbs.ui.activity.DateTimeActivity;
import com.geon.lbs.ui.activity.VehicleSearchActivity;
import com.geon.lbs.ui.customview.TransientDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.geon.lbs.ui.fragment.BaseMapFragment.OPMode.HISTORY;
import static com.geon.lbs.ui.fragment.BaseMapFragment.OPMode.HOME;
import static com.geon.lbs.ui.fragment.BaseMapFragment.OPMode.TRACKING;


/**
 * Created by Babu on 3/7/2018.
 */

public class VehicleOnMapFragment extends MapFragment implements View.OnClickListener{

    private final String TAG = "VehicleOnMapFragment";



    Button home,startTracking,showHistory,clearMap,mapNormal,mapSatellite;
    LinearLayout trackingInfoContainer;
    RelativeLayout historyInfoContainer;

    TextView selectedVehicleInTrackingInfo,locationIntrackingInfo,speedIntrackingInfo,acStatusIntrackingInfo,engineStatusIntrackingInfo;
    TextView selectedVehicleInHistoryInfo,dateIntrackingInfo,travelledDistanceIntrackingInfo;
    TransientDialog transientDialog;

    AllVehicleApi vehicleApi;
    LiveTrackingApi liveTrackingApi;
    HistoryDataApi historyDataApi;

    List<VehicleStatus> allVehicleStatusList;


    AppCompatActivity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.map_fragment,container,false);

        mGlobals = ((AppGlobal)getActivity().getApplicationContext());
        vehicleApi = new AllVehicleApi(getActivity());
        liveTrackingApi = new LiveTrackingApi(getActivity());
        historyDataApi = new HistoryDataApi();
        transientDialog = new TransientDialog(getActivity());


        Toolbar toolbar = (Toolbar)mView.findViewById(R.id.app_bar);
        activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("Home");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        buildGoogleApiClient();
        initView();
        if (checkPlayServices()) {
            Log.e("=====google api======","ase google api");
            buildGoogleApiClient();
        }
        Log.e(TAG,"onCreateView");
        OpearationMode = HOME ;
        return mView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tracking, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_select_vehicle:
                mMap.clear();
                stopAllBackgroundService();
                Intent i = new Intent(getActivity(),VehicleSearchActivity.class);
                startActivityForResult(i, 1);
                return true;
            default:
                break;
        }
        return false;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
        switch (OpearationMode){
            case HOME:
                startHomeOpearation();
                break;
            case TRACKING:
                startTrackingOpearation();
                break;
            case HISTORY:
                startHistoryOpearation();
                break;
            default:
                startHomeOpearation();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.map_normal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_satelite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.track_all_vehicle:
                startHomeOpearation();
                break;
            case R.id.start_tracking:
                startTrackingOpearation();
                break;
            case R.id.show_history:
                mMap.clear();
                stopAllBackgroundService();
                Intent mIntent = new Intent(getActivity(),DateTimeActivity.class);
                startActivityForResult(mIntent, 2);
                break;
            case R.id.clear_map:
                mMap.clear();
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == 1) {
                Log.e(TAG,"returned fromVehicleSearch Activity ");
            }
            else if (requestCode == 2) {
                Log.e(TAG,"returned DateTimeActivity Activity ");
                OpearationMode = HISTORY ;
            }
        }
    }
    void initView(){

        allVehicleStatusList = new ArrayList<VehicleStatus>();

        trackingInfoContainer  = (LinearLayout)mView.findViewById(R.id.tracing_info_container);
        historyInfoContainer = (RelativeLayout)mView.findViewById(R.id.history_info_container);


        selectedVehicleInTrackingInfo = (TextView)mView.findViewById(R.id.vehicle_id_in_trackinginfo);
        locationIntrackingInfo = (TextView)mView.findViewById(R.id.vehicle_location);
        speedIntrackingInfo = (TextView)mView.findViewById(R.id.speed_status);
        acStatusIntrackingInfo = (TextView)mView.findViewById(R.id.ac_status) ;
        engineStatusIntrackingInfo = (TextView)mView.findViewById(R.id.engine_status) ;

        selectedVehicleInHistoryInfo = (TextView)mView.findViewById(R.id.vehicle_id_histor_info);
        dateIntrackingInfo = (TextView)mView.findViewById(R.id.history_date);
        travelledDistanceIntrackingInfo = (TextView)mView.findViewById(R.id.traveled_distance);


        clearMap = (Button)mView.findViewById(R.id.clear_map);
        showHistory = (Button)mView.findViewById(R.id.show_history);
        startTracking = (Button)mView.findViewById(R.id.start_tracking);
        home = (Button)mView.findViewById(R.id.track_all_vehicle);

        mapNormal = mView.findViewById(R.id.map_normal);
        mapSatellite = mView.findViewById(R.id.map_satelite);


        clearMap.setOnClickListener(this);
        home.setOnClickListener(this);
        startTracking.setOnClickListener(this);
        showHistory.setOnClickListener(this);

        mapNormal.setOnClickListener(this);
        mapSatellite.setOnClickListener(this);

    }

    @Override
    void setHeaderVisibility(){
        trackingInfoContainer.setVisibility(View.GONE);
        historyInfoContainer.setVisibility(View.GONE);
        switch (OpearationMode){
            case HOME:
                break;
            case TRACKING:
                activity.getSupportActionBar().setTitle("Tracking");
                //trackingInfoContainer.setVisibility(View.VISIBLE);
                //selectedVehicleInHistoryInfo.setText(mGlobals.selectedVehicle);
                break;
            case HISTORY:
                activity.getSupportActionBar().setTitle("History");
                //historyInfoContainer.setVisibility(View.VISIBLE);
                //selectedVehicleInHistoryInfo.setText(mGlobals.selectedVehicle);
                //dateIntrackingInfo.setText("");
                //travelledDistanceIntrackingInfo.setText("");
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

    /*Home opearation is from here*/
    @Override
    void startHomeOpearation(){
        stopAllBackgroundService();
        if(null != mMap){
            mMap.clear();
        }
        OpearationMode = HOME;
        setHeaderVisibility();
        //Log.e("calling ..","All vehicle status");
        vehicleApi.downLoadAllVehicleStatus(new Callback<List<VehicleStatus>>() {
            @Override
            public void onSuccess(List<VehicleStatus> response) {
                if(!mGlobals.thread_for_allvehicle_api){return;}
                for(int i=0; i<response.size(); i++){

                    LocationData locationData = new LocationData();
                    locationData.setLatitude(response.get(i).getLatitude());
                    locationData.setLongitude(response.get(i).getLongitude());
                    locationData.setSpeed(response.get(i).getSpeed());
                    locationData.setAc_status(response.get(i).getAc_status());
                    locationData.setEngine_status(response.get(i).getEngine_status());
                    locationData.setData_status(response.get(i).getData_status());
                    locationData.setDevice_emei(response.get(i).getDevice_emei());

                    plotDataOnMap(locationData);

                    if(mGlobals.geonVehicleList.size()==1){
                        updateCurrentLocationInfo(locationData);
                    }
                }
                LatLng dhakaLatlong = new LatLng(23.777176,90.399452);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dhakaLatlong,7.0f));
            }
            @Override
            public void onError(String s) {
                Log.e(TAG,"ERROR");
            }
        });
    }

    /*Live Tracking functionally from here */
    @Override
    void startTrackingOpearation(){
        if(mGlobals.selectedVehicle.contentEquals("") || mGlobals.selectedVehicle.contentEquals("No Vehicel Selected")){
            transientDialog.showTransientDialog("","No vehicle is selected ");
            return;
        }
        stopAllBackgroundService();
        OpearationMode = TRACKING ;
        setHeaderVisibility();
        pLat=0.0;
        pLong = 0.0;
        mMap.clear();

        isFirstDataToPlot = true;

        liveTrackingApi.getVehicleCurrentStatus(new Callback<VehicleStatus>() {
            @Override
            public void onSuccess(VehicleStatus response) {
                if(!mGlobals.thread_for_livetracking_api){
                    return;
                }
                //Log.e("device_emai ",response.getDevice_emei());
                //Log.e("record_time ",response.getRecord_time());
                LocationData locationData = new LocationData();
                locationData.setLatitude(response.getLatitude());
                locationData.setLongitude(response.getLongitude());
                locationData.setSpeed(response.getSpeed());
                locationData.setData_status(response.getData_status());
                locationData.setEngine_status(response.getEngine_status());
                locationData.setAc_status(response.getAc_status());
                locationData.setRecord_date(response.getRecord_time());
                setCurrentPosition(locationData);
                updateCurrentLocationInfo(locationData);
            }
            @Override
            public void onError(String s) {

            }
        });
    }

    /*history opearation start from here*/
    @Override
    void startHistoryOpearation(){

        OpearationMode = HISTORY;
        stopAllBackgroundService();
        if(null != mMap){
            mMap.clear();
        }
        setHeaderVisibility();

        if(mGlobals.selectedVehicle.contentEquals("")
                || mGlobals.selectedVehicle.contains("No Vehicel Selected")){
            transientDialog.showTransientDialog("","No vehicle is selected ");
            return;
        }

        String vehicleImei = mGlobals.deviceVehiclePair.get(mGlobals.selectedVehicle);

        final ProgressDialog dialog = new ProgressDialog(getActivity(), R.style.StyledDialog);
        dialog.setMessage("Loading History Data For "+mGlobals.selectedVehicle+"....");
        dialog.setCancelable(false);
        dialog.show();

        historyDataApi.downLoadHistoryData(vehicleImei, mGlobals.startTime, mGlobals.endTime
                , new Callback<List<LocationData>>() {
                    @Override
                    public void onSuccess(List<LocationData> response) {

                        dialog.dismiss();
                        Log.e(TAG,"SUCCESS");
                        Log.e(TAG,response.size()+"");

                        double travelDistanace = 0;
                        if(response.size()>2){
                            travelDistanace = MapHelper.calculateDistance(response);
                        }

                        double intervalF = response.size()/300 ;
                        int interval = (intervalF>1.5) ? (int)intervalF:1;
                        List<LocationData> shortedLocationData = new ArrayList<>();
                        for(int i=0; i<response.size(); i=i+interval){
                            shortedLocationData.add(response.get(i));
                        }

                        Log.e("unshorted data size :",""+response.size());
                        Log.e("shorted data size :"," "+shortedLocationData.size());

                        for(int i=0; i<shortedLocationData.size(); i++){
                            //Log.e("lat",response.get(i).getLatitude())
                            plotHistoryData(shortedLocationData,shortedLocationData.get(i),i);
                        }
                        updateHistoryInfo(travelDistanace);
                    }
                    @Override
                    public void onError(String s) {
                        Log.e(TAG,"eRRor");
                        dialog.dismiss();
                        updateHistoryInfo(0);
                    }
                });
    }

    @Override
    void updateCurrentLocationInfo(LocationData locationDataToPlot){

       // Log.e("imei : ","imei " +locatonDataToPlot.getDevice_emei());

        trackingInfoContainer.setVisibility(View.VISIBLE);
        selectedVehicleInTrackingInfo.setText(mGlobals.selectedVehicle);
        locationIntrackingInfo .setText("");
        speedIntrackingInfo.setText(locationDataToPlot.getSpeed());
        if(locationDataToPlot.getAc_status().contentEquals("1")){
            acStatusIntrackingInfo.setText("ON");
        }else{
            acStatusIntrackingInfo.setText("OFF");
        }
        if(locationDataToPlot.getEngine_status().contentEquals("1")){
            engineStatusIntrackingInfo.setText("ON");
        }else{
            engineStatusIntrackingInfo.setText("OFF");
        }
        new RequestLocationAddressApi(getActivity()).requestLocationAddress(locationDataToPlot.getLatitudeDbl()
                ,locationDataToPlot.getLontitudeDbl(),new Callback<String>() {
            @Override
            public void onSuccess(String response) {
                locationIntrackingInfo .setText(response);
            }
            @Override
            public void onError(String s) {
                locationIntrackingInfo .setText(s);
            }
        });
    }
    void updateHistoryInfo(double travelledDistance){
        String strDateToShow = "";
        String endDateToShow = "";
        historyInfoContainer.setVisibility(View.VISIBLE);

        try{
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDateValue = input.parse(mGlobals.startTime);
            Date endDateValue = input.parse(mGlobals.endTime);

            SimpleDateFormat output = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
            strDateToShow = output.format(startDateValue);
            endDateToShow = output.format(endDateValue);

        }catch (Exception e){
            Log.e(TAG,"exception happend to parse date time");
            e.printStackTrace();
        }
        String s = "History From "+strDateToShow+ " To "+ endDateToShow;
        String t;
        if(travelledDistance == 0){
            t="No Data Found For This Vehicle";
        }else{
            t = "Travelled "+ Double.toString(travelledDistance)+" Km";
        }
        selectedVehicleInHistoryInfo.setText(mGlobals.selectedVehicle);
        dateIntrackingInfo.setText(s);
        travelledDistanceIntrackingInfo.setText(t);
    }
}

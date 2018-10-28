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
import com.geon.lbs.model.AllVehicleStatus;
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
import com.google.android.gms.maps.model.Marker;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
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

    List<LocationData> allVehicleStatusListLocationDataStatus;


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

    void initView(){

        allVehicleStatusListLocationDataStatus = new ArrayList<LocationData>();

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tracking, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_select_vehicle:
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
                Log.e("clicked","all_vehicle_function");
                startHomeOpearation();
                break;
            case R.id.start_tracking:
                Log.e("clicked","star_tracking_option");
                startTrackingOpearation();
                break;
            case R.id.show_history:
                Intent mIntent = new Intent(getActivity(),DateTimeActivity.class);
                startActivityForResult(mIntent, 2);
                break;
            case R.id.clear_map:
                clearMap();
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == 1) {
                //Log.e(TAG,"returned fromVehicleSearch Activity ");
                String selectedDeviceImei = "";
                LocationData selectedLocationData = new LocationData() ;
                boolean hasLocationDate = false;

                for (Map.Entry<String,String> entry : mGlobals.deviceVehiclePair.entrySet()) {
                    String imeiToCheck = entry.getKey();
                    if(imeiToCheck.contentEquals(mGlobals.selectedVehicle)){
                        selectedDeviceImei = entry.getValue();
                        //Log.e(TAG,"selectedDeviceImei: "+selectedDeviceImei);
                        break;
                    }
                }
                for(LocationData vStatus:allVehicleStatusListLocationDataStatus){
                    //Log.e(TAG,"selectedImei "+" "+vStatus.getDevice_emei());
                    if(vStatus.getDevice_emei().contentEquals(selectedDeviceImei)){
                        //Log.e(TAG,"selecteVehicleObj: "+" "+vStatus.getDevice_emei());
                        selectedLocationData = vStatus;
                        hasLocationDate = true;
                        break;
                    }
                }
                if(hasLocationDate){
                    updateCurrentLocationInfo(selectedLocationData);
                    updateLocationAddress(selectedLocationData);
                }
                switch (OpearationMode){
                    case HOME:
                        if(hasLocationDate){
                            for(Marker m:allVehiclesMarkers){
                                if (m.getTag().equals(selectedLocationData)) {
                                    m.setTitle( mGlobals.selectedVehicle);
                                    m.showInfoWindow();
                                    LatLng mLatlng = new LatLng(selectedLocationData.getLatitudeDbl(), selectedLocationData.getLontitudeDbl());
                                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatlng,16.50f));
                                }
                            }
                        }
                    case TRACKING:
                        break;
                }
            }
            else if (requestCode == 2) {
                //Log.e(TAG,"returned DateTimeActivity Activity ");
                OpearationMode = HISTORY ;
            }
        }
        else if(resultCode == RESULT_CANCELED){
            if (requestCode == 1) {
                //Log.e(TAG,"returned fromVehicleSearch Activity ");
            }
            else if (requestCode == 2) {
                //Log.e(TAG,"no history time is selected  ");
            }
        }
    }

    @Override
    void setHeaderVisibility(){
        trackingInfoContainer.setVisibility(View.GONE);
        historyInfoContainer.setVisibility(View.GONE);
        switch (OpearationMode){
            case HOME:
                activity.getSupportActionBar().setTitle("Home");
                break;
            case TRACKING:
                activity.getSupportActionBar().setTitle("Tracking");
                break;
            case HISTORY:
                activity.getSupportActionBar().setTitle("History");
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
    void setButtonBackgroundNotSelected(){
        clearMap.setBackgroundResource(R.drawable.btn_clear_gray);
        showHistory.setBackgroundResource(R.drawable.btn_history_gray);
        startTracking.setBackgroundResource(R.drawable.btn_tracking_gray);
        home.setBackgroundResource(R.drawable.btn_home_gray);
    }

    @Override
    void startHomeOpearation(){

        setButtonBackgroundNotSelected();
        home.setBackgroundResource(R.drawable.btn_home_selected);
        if(mGlobals.thread_for_allvehicle_api){
            Log.e("theread","theread of all vehicle is active");
            return;
        }

        stopAllBackgroundService();
        if(null != mMap){
            clearMap();
        }
        OpearationMode = HOME;
        setHeaderVisibility();
        //Log.e("calling ..","All vehicle status");
        mGlobals.thread_for_allvehicle_api=true;
        vehicleApi.downLoadAllVehicleStatus(new Callback<List<VehicleStatus>>() {
            @Override
            public void onSuccess(List<VehicleStatus> response) {

                if(!mGlobals.thread_for_allvehicle_api){
                    return;
                }

                allVehicleStatusListLocationDataStatus.clear();
                allVehiclesMarkers.clear();

                for(int i=0; i<response.size(); i++){

                    LocationData locationData = new LocationData();
                    locationData.setLatitude(response.get(i).getLatitude());
                    locationData.setLongitude(response.get(i).getLongitude());
                    locationData.setSpeed(response.get(i).getSpeed());
                    locationData.setAc_status(response.get(i).getAc_status());
                    locationData.setEngine_status(response.get(i).getEngine_status());
                    locationData.setData_status(response.get(i).getData_status());
                    locationData.setDevice_emei(response.get(i).getDevice_emei());

                    allVehicleStatusListLocationDataStatus.add(locationData);

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
                //Log.e(TAG,"ERROR");
            }
        });
    }

    @Override
    void startTrackingOpearation(){

        if(mGlobals.selectedVehicle.contentEquals("") || mGlobals.selectedVehicle.contentEquals("No Vehicel Selected")){
            transientDialog.showTransientDialog("","No vehicle is selected ");
            return;
        }
        setButtonBackgroundNotSelected();
        startTracking.setBackgroundResource(R.drawable.btn_tracking_selected);
        clearMap();
        pLat=0.0;
        pLong = 0.0;
        isFirstDataToPlot = true;

        if(mGlobals.thread_for_livetracking_api){
            //Log.e("Thread ","theread_for_livetr_active");
            return;
        }

        stopAllBackgroundService();

        OpearationMode = TRACKING ;
        setHeaderVisibility();

        mGlobals.thread_for_livetracking_api=true;

        liveTrackingApi.getVehicleCurrentStatus(new Callback<VehicleStatus>() {
            @Override
            public void onSuccess(VehicleStatus response) {

                if(!mGlobals.thread_for_livetracking_api){
                    return;
                }

                String recentImei = mGlobals.deviceVehiclePair.get(mGlobals.selectedVehicle);
                if(!response.getDevice_emei().contentEquals(recentImei)){
                    //Log.e(TAG,"not selected vehicles info");
                    return;
                }

                LocationData locationData = new LocationData();
                locationData.setLatitude(response.getLatitude());
                locationData.setLongitude(response.getLongitude());
                locationData.setSpeed(response.getSpeed());
                locationData.setData_status(response.getData_status());
                locationData.setEngine_status(response.getEngine_status());
                locationData.setAc_status(response.getAc_status());
                locationData.setRecord_date(response.getRecord_time());

                if(pLat == locationData.getLatitudeDbl() && pLong == locationData.getLontitudeDbl()){
                    return;
                }
                if(isFirstDataToPlot){
                    updateLocationAddress(locationData);
                }
                setCurrentPosition(locationData);
                updateCurrentLocationInfo(locationData);
            }
            @Override
            public void onError(String s) {

            }
        });
    }

    @Override
    void startHistoryOpearation(){

        OpearationMode = HISTORY;
        stopAllBackgroundService();
        if(null != mMap){
            clearMap();
        }
        setHeaderVisibility();
        setButtonBackgroundNotSelected();
        showHistory.setBackgroundResource(R.drawable.btn_history_selected);

        if(mGlobals.selectedVehicle.contentEquals("")
                || mGlobals.selectedVehicle.contains("No Vehicel Selected")){
            transientDialog.showTransientDialog("","No vehicle is selected ");
            return;
        }

        String vehicleImei = mGlobals.deviceVehiclePair.get(mGlobals.selectedVehicle);

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading History Data ....");
        dialog.setCancelable(false);
        dialog.show();

        historyDataApi.downLoadHistoryData(vehicleImei, mGlobals.startTime, mGlobals.endTime
                , new Callback<List<LocationData>>() {
                    @Override
                    public void onSuccess(List<LocationData> response) {

                        dialog.dismiss();
                        //Log.e(TAG,"SUCCESS");
                        //Log.e(TAG,response.size()+"");

                        double travelDistanace = 0;
                        if(response.size()>2){
                            travelDistanace = MapHelper.calculateDistance(response);
                        }

                        double intervalF = response.size()/350 ;
                        int interval = (intervalF>1.5) ? (int)intervalF:1;
                        List<LocationData> shortedLocationData = new ArrayList<>();
                        for(int i=0; i<response.size(); i=i+interval){
                            shortedLocationData.add(response.get(i));
                        }

                        //Log.e("unshorted data size :",""+response.size());
                        //Log.e("shorted data size :"," "+shortedLocationData.size());

                        for(int i=0; i<shortedLocationData.size(); i++){
                            //Log.e("lat",response.get(i).getLatitude())
                            plotHistoryData(shortedLocationData,shortedLocationData.get(i),i);
                        }
                        updateHistoryInfo(travelDistanace);
                    }
                    @Override
                    public void onError(String s) {
                        //Log.e(TAG,"eRRor");
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
       // locationIntrackingInfo .setText("Click Marker For Vehicle Address");
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
            //Log.e(TAG,"exception happend to parse date time");
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
    @Override
    void updateLocationAddress(LocationData locationDataToPlot){

        try{
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
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    void clearMap(){
        clearMap.setBackgroundResource(R.drawable.btn_clear_selected);
        mMap.clear();
        clearMap.setBackgroundResource(R.drawable.btn_clear_gray);
    }
}

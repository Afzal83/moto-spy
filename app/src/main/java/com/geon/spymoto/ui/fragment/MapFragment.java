package com.geon.spymoto.ui.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geon.spymoto.AppGlobal;
import com.geon.spymoto.R;
import com.geon.spymoto.helper.Constants;
import com.geon.spymoto.helper.ConstantsFLA;
import com.geon.spymoto.helper.HttpClient;
import com.geon.spymoto.helper.MapHelper;
import com.geon.spymoto.helper.Places;
import com.geon.spymoto.model.LocationData;
import com.geon.spymoto.model.NearestLocation;
import com.geon.spymoto.model.VehicleStatus;
import com.geon.spymoto.services.FetchAddressIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;


import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static com.geon.spymoto.ui.customview.MarkarBitmap.getRoundedCroppedBitmap;

/**
 * Created by Babu on 3/7/2018.
 */

public class MapFragment extends BaseFragment implements
        OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks
        ,GoogleApiClient.OnConnectionFailedListener
        ,GoogleMap.OnMarkerClickListener
        ,GoogleMap.OnMapClickListener{

    private static final String TAG = "TrackAllVehicleFragment";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;


    SupportMapFragment fragment;
    private GoogleApiClient mGoogleApiClient;
    public GoogleMap mMap ;

    AppGlobal mGlobals;
    boolean isFirstDataToPlot = true;
    double pLat=0.0;
    double pLong = 0.0;

    private double currentMarkerLat=0.0;
    private double currentMarkerLong=0.0;
    private Marker currentMarker ;

    String realAddress= "";
    private List<NearestLocation> nearestLocatoinList = new ArrayList<>()  ;

    LinearLayout speedContainer_c;
    TextView vehicleSpeed_tv;
    ProgressDialog pDialog;
    View mView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach: context: " + context.toString());
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);// this line may not needed..
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if (mMap == null) {
            fragment.getMapAsync(this);
        }
        mGoogleApiClient.connect();
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = android.support.v4.app.Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.setPadding(0, 30, 30, 30);
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMapClickListener(this);


        LatLng mLatlng = new LatLng(23.746585,90.392234);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatlng,7.0f));
    }

    @Override
    public void onConnected(Bundle bundle) {
        //   showMyLocation();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker){

        Log.e("markerclicked","markerclicked");
        if(marker == null){return  true;}
        mMap.getUiSettings().setMapToolbarEnabled(true);

        switch (OpearationMode){
            case HOME:
                VehicleStatus locatonDataToPlot = ( VehicleStatus) marker.getTag();
                mGlobals.selectedVehicle = locatonDataToPlot.getNumberPlate();
                //Log.e("selected vehicle : ",mGlobals.selectedVehicle);
                startTrackingOpearation();
                break;
            case TRACKING:
                currentMarker = marker;
                LocationData locationData = (LocationData)marker.getTag();
                currentMarkerLat = locationData.getLatitudeDbl();
                currentMarkerLong = locationData.getLontitudeDbl();
                realAddress= "";
                nearestLocatoinList.clear();
                getLocationAddress();
                break;
            case HISTORY:
                break;
            default:
                break;

        }
        return true;
    }
    public boolean checkPlayServices() {
        Log.e("google api=========","ase google api");
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getActivity().getApplicationContext(),"This device is not supported.", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    void plotDataOnMap(VehicleStatus locatonDataToPlot){
        try {
            double lat = locatonDataToPlot.getLatDouble();
            double lon = locatonDataToPlot.getLongitudeDouble();

            LatLng mLatlng = new LatLng(lat,lon);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(mLatlng)
                    .icon(BitmapDescriptorFactory.fromBitmap(myImage(locatonDataToPlot)))
                    .anchor(0.5f, 1));
            marker.setTag(locatonDataToPlot);
            LatLng dhakaLatlong = new LatLng(23.777176,90.399452);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dhakaLatlong,7.0f));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    Bitmap myImage(VehicleStatus mLocationData){

        double speed = Double.parseDouble(mLocationData.getSpeed());
        String numberPlate = mLocationData.getNumberPlate();
        String engineStatus = mLocationData.getEngine_status();
        Bitmap A = getRoundedCroppedBitmap(speed,numberPlate);
        Bitmap B;

        if (engineStatus.contains("0")){
            B= BitmapFactory.decodeResource(getResources(), R.drawable.icon_stop);
        }else if (speed == 0.0) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_1_10);
        } else if (speed > 0 && speed <= 10) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_1_10);
        } else if (speed > 10 && speed <= 20) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_11_20);
        } else if (speed > 20 && speed <= 30) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_21_30);
        } else if (speed > 30 && speed <= 40) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_31_40);
        } else if (speed > 40 && speed <= 50) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_41_50);
        } else if (speed> 50 && speed <= 60) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_51_60);
        } else if (speed > 60 && speed <= 70) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_61_70);
        } else if (speed > 70 && speed <= 80) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_71_80);
        } else if (speed > 80 && speed <= 90) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_81_90);
        } else if (speed > 90 && speed <= 100) {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_91_100);
        } else {
            B= BitmapFactory.decodeResource(getResources(),R.drawable.icon_111_150);
        }
        Canvas canvas = new Canvas(A);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(B,90,60, paint);
        return A;
    }





    public void setCurrentPosition(LocationData locationData) {
        LatLng mLatlng = new LatLng(locationData.getLatitudeDbl(), locationData.getLontitudeDbl());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatlng,16.50f));
        if (!isFirstDataToPlot) {
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(pLat, pLong), new LatLng(locationData.getLatitudeDbl(), locationData.getLontitudeDbl()))
                    .width(12)
                    .color(Color.RED));

            LatLng prePositionLatLong = new LatLng(pLat,pLong);
            double distance = MapHelper.CalculateDistance(prePositionLatLong,mLatlng);
            Log.e("....DISTANCE....","D:"+distance);
            if(distance > 0.10){
                MarkerOptions arroMarkerOption = new MarkerOptions();
                LatLng midLatlng = MapHelper.midPoint(pLat,pLong,locationData.getLatitudeDbl(), locationData.getLontitudeDbl());
                arroMarkerOption.position(midLatlng);
                Marker ArrowMarker = mMap.addMarker(arroMarkerOption);
                ArrowMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow));
                LatLng fromLatLong = new LatLng(pLat,pLong);
                double HeadingRotation = SphericalUtil.computeHeading(fromLatLong,mLatlng);
                ArrowMarker.setRotation((float)HeadingRotation);
            }
        }
        //show directional arrow **************************end***************
        isFirstDataToPlot = false;

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mLatlng);
        Marker marker = mMap.addMarker(markerOptions);
        markerOptions.title(locationData.getRecord_time());
        marker.setTag(locationData);


        speedContainer_c.setVisibility(View.VISIBLE);

        if(locationData.getEngine_status().contains("0")){
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_stop));
            speedContainer_c.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_1_10));
        }
        else if (locationData.getSpeedDbl() == 0) {
            speedContainer_c.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_1_10));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_1_10));
        }
        else if (locationData.getSpeedDbl()  > 0 && locationData.getSpeedDbl()  <= 10) {
            speedContainer_c.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_1_10));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_1_10));
        } else if (locationData.getSpeedDbl() > 10 && locationData.getSpeedDbl()  <= 20) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_11_20));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_11_20));
        } else if (locationData.getSpeedDbl() > 20 && locationData.getSpeedDbl() <= 30) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_21_30));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_21_30));
        } else if (locationData.getSpeedDbl() > 30 && locationData.getSpeedDbl() <= 40) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_31_40));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_31_40));
        } else if (locationData.getSpeedDbl() > 40 && locationData.getSpeedDbl() <= 50) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_41_50));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_41_50));
        } else if (locationData.getSpeedDbl() > 50 && locationData.getSpeedDbl() <= 60) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_51_60));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_51_60));
        } else if (locationData.getSpeedDbl() > 60 && locationData.getSpeedDbl() <= 70) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_61_70));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_61_70));
        } else if (locationData.getSpeedDbl() > 70 && locationData.getSpeedDbl() <= 80) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_71_80));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_71_80));
        } else if (locationData.getSpeedDbl() > 80 && locationData.getSpeedDbl() <= 90) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_81_90));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_81_90));
        } else if (locationData.getSpeedDbl() > 90 && locationData.getSpeedDbl() <= 100) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_100_150));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_91_100));
        } else if (locationData.getSpeedDbl() > 100 && locationData.getSpeedDbl() <= 110) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_100_150));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_91_100));
        } else if (locationData.getSpeedDbl() > 110 && locationData.getSpeedDbl() <= 120) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_100_150));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_111_150));
        } else if (locationData.getSpeedDbl() > 120 && locationData.getSpeedDbl() <= 130) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_100_150));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_111_150));
        } else if (locationData.getSpeedDbl() > 130 && locationData.getSpeedDbl() <= 140) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_100_150));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_111_150));
        } else if (locationData.getSpeedDbl() > 140 && locationData.getSpeedDbl() <= 150) {
            speedContainer_c.setBackground( ContextCompat.getDrawable(getActivity(), R.drawable.speed_container_100_150));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_111_150));
        }
        marker.setTitle(locationData.getRecord_time());
        marker.showInfoWindow();

        Log.e("enginestatus----:",locationData.getEngine_status());
        if(locationData.getEngine_status().contentEquals("0")){
            vehicleSpeed_tv.setText("STOP");
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.icon_stop));
        }else{
            String spdV = String.valueOf(locationData.getSpeed());
            vehicleSpeed_tv.setText(spdV);
        }

        pLat = locationData.getLatitudeDbl();
        pLong = locationData.getLontitudeDbl();
    }

    void plotHistoryData(List<LocationData> historyDataList, LocationData locatonDataToPlot, int i){

        LatLng mLatlng = new LatLng(locatonDataToPlot.getLatitudeDbl(),locatonDataToPlot.getLontitudeDbl());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mLatlng);
        markerOptions.title(locatonDataToPlot.getRecord_time());
        Log.e("Location data time",locatonDataToPlot.getRecord_time());

        if(i == (historyDataList.size()-1)){
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(locatonDataToPlot);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatlng,15.0f));
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow_for_marker));
        }
        else if(i==0){
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(locatonDataToPlot);
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow_for_marker));
        }
        else if(i%20 == 0){
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTag(locatonDataToPlot);
            LatLng fromLatLong = new LatLng(historyDataList.get(i-1).getLatitudeDbl()
                    ,historyDataList.get(i-1).getLontitudeDbl());
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_arrow_for_marker));
            double HeadingRotation = SphericalUtil.computeHeading(fromLatLong,mLatlng);
            marker.setRotation((float)HeadingRotation);
        }
        if(i>0){
            if(historyDataList.get(i-1).getLatitude()== historyDataList.get(i).getLatitude()
                    && historyDataList.get(i-1).getLongitude()== historyDataList.get(i).getLongitude()){
                return ;
            }
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(new LatLng(historyDataList.get(i-1).getLatitudeDbl()
                                    ,historyDataList.get(i-1).getLontitudeDbl()),
                            new LatLng(historyDataList.get(i).getLatitudeDbl(),historyDataList.get(i).getLontitudeDbl()))
                    .width(12)
                    .color(Color.RED));
        }
    }




    void getLocationAddress(){
        LatLng latLng = new LatLng(currentMarkerLat,currentMarkerLong);
        if (Geocoder.isPresent()) {
            startIntentService(latLng);
            Log.e("geocoder","present");
        }else{
            Toast.makeText(getActivity(),"Geocodar in not present !!!", Toast.LENGTH_LONG).show();
            Log.e("geocoder","notpresent");
        }
    }
    protected void startIntentService(LatLng latLng) {
        Intent intent = new Intent(getActivity(),FetchAddressIntentService.class);
        Bundle mBundle=new Bundle();
        mBundle.putParcelable(ConstantsFLA.LOCATION_DATA_EXTRA,latLng);
        mBundle.putParcelable(ConstantsFLA.RECEIVER, new AddressResultReceiver(new Handler()));
        intent.putExtra("bundleData", mBundle);
        getActivity().startService(intent);
    }
    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            realAddress = resultData.getString(ConstantsFLA.RESULT_DATA_KEY);
            if (resultCode == ConstantsFLA .SUCCESS_RESULT) {
                getNearByLocatons();
                Log.e("place parser result","present");
            }
            else{
                Toast.makeText(getActivity(),"Geocodar Error !!!", Toast.LENGTH_LONG).show();
                Log.e("place parser result","empty");
            }
        }
    }
    void getNearByLocatons(){
        String type =  "street"+"highway"+"home_goods_store"+"hospital"+"atm"+"bank"+"restaurant"+"atm";

        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        sb.append("location="+currentMarkerLat+","+currentMarkerLong);
        sb.append("&radius=5000");
        sb.append("&types="+type);
        sb.append("&sensor=true");
        sb.append("&key="+ Constants.API_KEY);

        // Creating a new non-ui thread task to download json data
        PlacesTask placesTask = new PlacesTask();

        // Invokes the "doInBackground()" method of the class PlaceTask
        placesTask.execute(sb.toString());

    }
    /** A class, to download Google Places */
    private class PlacesTask extends AsyncTask<String, Integer, String> {
        String data = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading....");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... url) {
            HttpClient mClient = new HttpClient();
            try {
                data = mClient.downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
        @Override
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            Places nearestPlaces = new Places();
            try {
                JSONObject googlePlacesJson = new JSONObject(data);
                nearestLocatoinList = nearestPlaces.parseLocationResult(googlePlacesJson);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (null != nearestLocatoinList) {
                for (int i = 0; i < nearestLocatoinList.size(); i++) {
                    if (i == 6) {
                        break;
                    } else {
                        double dist = getDistanceOfNearestLocation(nearestLocatoinList.get(i));
                        nearestLocatoinList.get(i).distance = dist;
                    }
                }

                for (NearestLocation mList : nearestLocatoinList) {
                    Log.e("place name", mList.PlaceName);
                    Log.e("Lat", mList.Latitude + "");
                    Log.e("Long", mList.Longitude + "");
                }

                if (!nearestLocatoinList.isEmpty()) {
                    showNearbyLocation(nearestLocatoinList);
                } else {
                    Toast.makeText(getActivity(), "Nearest place is not available !!!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    double getDistanceOfNearestLocation(NearestLocation mLocation) {
        LatLng to = new LatLng(mLocation.Latitude, mLocation.Longitude);
        LatLng from = new LatLng(currentMarkerLat, currentMarkerLong);
        Double distance = MapHelper.CalculateDistance(from,to);
        return distance;
    }
}

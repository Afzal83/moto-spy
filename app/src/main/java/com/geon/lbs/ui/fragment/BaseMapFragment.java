package com.geon.lbs.ui.fragment;


import android.support.v4.app.Fragment;

import com.geon.lbs.model.LocationData;
import com.geon.lbs.model.NearestLocation;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Babu on 3/7/2018.
 */

public class BaseMapFragment extends Fragment {

    List<Marker> allVehiclesMarkers = new ArrayList<>();

    enum OPMode
    {
        HOME,TRACKING,HISTORY;
    }
    OPMode OpearationMode;

    void setHeaderVisibility(){}
    void startHomeOpearation(){}
    void startTrackingOpearation(){}
    void startHistoryOpearation(){}
    void stopAllBackgroundService(){}
    void updateCurrentLocationInfo(LocationData locationData){};
    void updateLocationAddress(LocationData locationDataToPlot){};
}

package com.geon.spymoto.ui.fragment;


import android.support.v4.app.Fragment;

import com.geon.spymoto.model.NearestLocation;

import java.util.List;

/**
 * Created by Babu on 3/7/2018.
 */

public class BaseFragment extends Fragment {

    enum OPMode
    {
        HOME,TRACKING,HISTORY;
    }
    OPMode OpearationMode;

    void setSelectedVehicle(){

    }
    void setHeaderVisibility(){

    }

    void startHomeOpearation(){}
    void startTrackingOpearation(){}
    void startHistoryOpearation(){}

    void showNearbyLocation(List<NearestLocation> nearestLocatoinList){}


    void stopAllBackgroundService(){}


}

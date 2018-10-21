package com.geon.lbs;

import android.app.Application;

import com.geon.lbs.model.Vehicle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Babu on 3/29/2018.
 */

public class AppGlobal extends Application {



    public boolean isUserLogedin = false ;
    public String userName = "";
    public String userPassword = "";
    public String userCatagory = "";
    public String userFirsName = "";
    public String userLastName = "";
    public String userAddressLineOne = "";
    public String userAddressLineTwo = "";
    public String userPostCode = "";
    public String city = "";
    public String userPhone = "";
    public String userImage = "";
    public String userEmail = "";

    public List<String> allVehicleList = new ArrayList<String>() ;
    public List<String> allAccounts = new ArrayList<String>();
    public String selectedVehicle = "No Vehicel Selected";

    public String startTime = "Start Time :";
    public String endTime = "End Time :";

    public List<Vehicle>  geonVehicleList = new ArrayList<>();
    public HashMap<String,String> deviceVehiclePair=new HashMap<String,String>();

    public boolean thread_for_allvehicle_api = false;
    public boolean thread_for_livetracking_api = false;
}

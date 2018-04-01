package com.geon.spymoto.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.geon.spymoto.R;
import com.geon.spymoto.ui.fragment.SelectVehicleFragment;


public class VehicleSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vehicle_search);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.vehicle_search_container,new SelectVehicleFragment()).commit();
    }
}

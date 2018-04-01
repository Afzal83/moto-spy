package com.geon.spymoto.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.geon.spymoto.R;
import com.geon.spymoto.ui.fragment.SelectDurationFragment;


public class TrackingDuration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tracking_duration);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.select_history_duration_fragment,new SelectDurationFragment()).commit();
    }
}

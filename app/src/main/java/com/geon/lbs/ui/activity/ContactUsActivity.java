package com.geon.lbs.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.geon.lbs.R.layout.activity_contact_us);
         getSupportFragmentManager().beginTransaction()
                .replace(com.geon.lbs.R.id.fragment_container,new com.geon.lbs.ui.fragment.AboutUsFragment()).commit();
    }
}

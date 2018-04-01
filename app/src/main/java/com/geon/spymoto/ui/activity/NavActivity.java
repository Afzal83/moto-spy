package com.geon.spymoto.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.geon.spymoto.AppGlobal;
import com.geon.spymoto.R;
import com.geon.spymoto.helper.Constants;
import com.geon.spymoto.helper.TimeHelper;
import com.geon.spymoto.ui.fragment.VehicleOnMapFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NavActivity extends AppCompatActivity {

    private static final String TAG = "NavigationActivity";
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private Button actionBtn;
    NavigationView navigationView;
    AppGlobal mGlobals;
    TextView mTitle ;
    int mStackLevel;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: ");
        setContentView(R.layout.activity_nav);
        mGlobals = ((AppGlobal)getApplicationContext());

        setHistoryDefaultTime();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container,new VehicleOnMapFragment()).commit();
        initNavigationDrawer();

        actionBtn = (Button )findViewById(R.id.actionBarButton);
        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    public void initNavigationDrawer() {
        navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                Log.e(TAG, "onNavigationItemSelected: id: " + id);
                switch (id){
                    case R.id.Profile:
                        gotoFragment("goto_profileFragment");
                        break;
                    case R.id.about_us:
                        gotoFragment("goto_aboutus");
                        break;
                    case R.id.Logout:
                        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = mSharedPreference.edit();
                        editor.putString(Constants.USER_NAME,"");
                        editor.putString(Constants.USER_PASSWORD,"");
                        editor.putBoolean(Constants.USER_IS_LOGGEDIN,false);
                        editor.apply();

                        FragmentManager fm = getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                            fm.popBackStack();
                        }
                        Log.e(TAG, "onNavigationItemSelected: going to login activity ");

                        startActivity(new Intent(NavActivity.this,LoginActivity.class));
                        NavActivity.this.finish();

                        break;
                    default:
                        Log.e(TAG, "onNavigationItemSelected: default");
                        break;

                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
        //View header = navigationView.getHeaderView(0);
        //TextView tv_email = (TextView)header.findViewById(R.id.tv_email);
        //ImageView userImge = (ImageView)header.findViewById(R.id.header_image);
        // userImge.setImageResource(R.drawable.mr_tasfeen);
        //String fullName = mGlobals.userFirsName + " "+mGlobals.userLastName;
        //tv_email.setText("hello user");
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.drawer_open,R.string.drawer_close){

            View v = NavActivity.this.getCurrentFocus();
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                Log.e("drawer closed","drawer closed");
            }
            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
                Log.e("drawer open","drawer open");
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void replaceFragment(Fragment fragment, String FRAGMENT_TAG) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment, FRAGMENT_TAG);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG) == null) {
            Log.e("Inside Fragment add", "ooooooooooooooooooooooooooooooooooooooooooooooooo");
            fragmentTransaction.addToBackStack(FRAGMENT_TAG);
        }
        fragmentTransaction.commit();
    }

    void gotoFragment(String fragment) {
        mGlobals.thread_for_allvehicle_api = false;
        mGlobals.thread_for_history_animation = false ;
        mGlobals.thread_for_livetracking_api = false;

        Log.e(TAG, "gotoFragment: fragment name: " + fragment);
        getSupportFragmentManager().addOnBackStackChangedListener(backStackListener);
    }

    void setHistoryDefaultTime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mGlobals.endTime  = dt.format(calendar.getTime());
        mGlobals.startTime = dt.format(TimeHelper.getStartTimeOfADay(calendar.getTime()));
    }




    private FragmentManager.OnBackStackChangedListener backStackListener =  new FragmentManager.OnBackStackChangedListener() {

        @Override
        public void onBackStackChanged() {
            Log.e("fragment changed",">>>>>>>>>>>>>>fragmentchanged<<<<<<<<");
        }
    };

    Fragment getCurrentFragment()
    {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.main_container);
        return currentFragment;
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy: ");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        mGlobals.thread_for_allvehicle_api = false;
        mGlobals.thread_for_history_animation =false ;
        mGlobals.thread_for_livetracking_api = false;


        Log.e(TAG,"total fragment in backstack :"+getSupportFragmentManager().getBackStackEntryCount());
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){

        }
        else{
            super.onBackPressed();
            finish();
        }
    }
}

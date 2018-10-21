package com.geon.lbs.ui.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geon.lbs.AppGlobal;
import com.geon.lbs.R;
import com.geon.lbs.helper.TimeHelper;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = "DateTimeActivity";

    CheckBox durationOneHour,duarationTwoHour,durationSixHOur,durationTwelveHour,durationTwentyFourHour
            ,durationTodya,durationYesterDay,customDuration;




    int selectedCheckBox = 0;
    String customStartDate = "";
    String customEndDate = "";
    int customDateType = 0;
    boolean isTimeCustom = false;

    AppGlobal  mGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_date_time);

        //DisplayMetrics metrics = getResources().getDisplayMetrics();
        //int screenWidth = (int) (metrics.widthPixels * 0.95);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        mGlobals = ((AppGlobal)this.getApplicationContext());

        //setDateTimePicker();
        bindView();
    }
    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        durationOneHour.setChecked(false);
        duarationTwoHour.setChecked(false);
        durationSixHOur.setChecked(false);
        durationTwelveHour.setChecked(false);
        durationTwentyFourHour.setChecked(false);
        durationTodya.setChecked(false);
        durationYesterDay.setChecked(false);
        customDuration.setChecked(false);

        switch (view.getId()){
            case R.id.duration_1h:

                if(checked){
                    durationOneHour.setChecked(true);
                    selectedCheckBox = 1;
                }
                else {
                    durationOneHour.setChecked(false);
                }

                break;
            case R.id.duration_2h:
                if(checked){

                    selectedCheckBox = 2 ;
                    duarationTwoHour.setChecked(true);
                }
                else {
                    duarationTwoHour.setChecked(false);
                }
                break;
            case R.id.duration_6h:
                if(checked){

                    selectedCheckBox = 3;
                    durationSixHOur.setChecked(true);
                }
                else {
                    durationSixHOur.setChecked(false);
                }
                break;
            case R.id.duration_12h:
                if(checked){
                    selectedCheckBox =4 ;
                    durationTwelveHour.setChecked(true);
                }
                else {durationTwelveHour.setChecked(false);}
                break;
            case R.id.duration_24h:
                if(checked){
                    selectedCheckBox = 5;
                    durationTwentyFourHour.setChecked(true);
                }
                else {durationTwentyFourHour.setChecked(false);}
                break;
            case R.id.duration_today:
                if(checked){
                    selectedCheckBox = 6;
                    durationTodya.setChecked(true);
                }
                else {durationTodya.setChecked(false);}
                break;
            case R.id.duration_yesterday:
                if(checked){
                    selectedCheckBox = 7;
                    durationYesterDay.setChecked(true);
                }
                else {durationYesterDay.setChecked(false);}
                break;
            case R.id.duration_custom:
                if(checked){
                    isTimeCustom = true;
                    selectedCheckBox = 8;
                    customDuration.setChecked(true);
                    setDateTimePicker();
                }
                else {
                    customDuration.setChecked(false);
                }
                break;
        }
        if(view.getId() != R.id.duration_custom){
            isTimeCustom =false;
            processDuration();
        }
    }
    void bindView(){
        durationOneHour = (CheckBox) findViewById(R.id.duration_1h);
        duarationTwoHour = (CheckBox) findViewById(R.id.duration_2h);
        durationSixHOur = (CheckBox) findViewById(R.id.duration_6h);
        durationTwelveHour = (CheckBox) findViewById(R.id.duration_12h);
        durationTwentyFourHour = (CheckBox) findViewById(R.id.duration_24h);
        durationTodya = (CheckBox) findViewById(R.id.duration_today);
        durationYesterDay = (CheckBox) findViewById(R.id.duration_yesterday);
        customDuration = (CheckBox) findViewById(R.id.duration_custom);

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //String dateS = Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(dayOfMonth);
        //Log.e("selected date : ",dateS);

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.YEAR,year);
        startTime.set(Calendar.MONTH,month);
        startTime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        startTime.set(Calendar.HOUR_OF_DAY, 0);// for 6 hour
        startTime.set(Calendar.MINUTE, 0);// for 0 min
        startTime.set(Calendar.SECOND, 0);// for 0 sec

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.YEAR,year);
        endTime.set(Calendar.MONTH,month);
        endTime.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        endTime.set(Calendar.HOUR_OF_DAY, 23);// for 6 hour
        endTime.set(Calendar.MINUTE, 59);// for 0 min
        endTime.set(Calendar.SECOND, 59);// for 0 sec


        try {
            customStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTime.getTime());
            customEndDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTime.getTime());
            Log.e("customStartDate",customStartDate);
            Log.e("customEndDate",customEndDate);
        } catch (Exception e) {
            Log.e("exception ","date format");
        }
        processDuration();
    }

    void setDateTimePicker(){
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }


    void  processDuration(){
        boolean isDurationSelectionOk =true;
        String startTime = "";
        String endTime = "";
        if (isTimeCustom){
            if(customStartDate.isEmpty() || customStartDate.contentEquals("")){
                isDurationSelectionOk =false;
                Toast.makeText(this,"Please Select Start Time", Toast.LENGTH_LONG).show();
            }
            else if(customEndDate.isEmpty()|| customEndDate.contentEquals("")){
                isDurationSelectionOk =false;
                Toast.makeText(this,"Please Select End Time", Toast.LENGTH_LONG).show();
            }
            else{
                startTime = customStartDate;
                endTime = customEndDate;
                float timeDifference = TimeHelper.timeDifference(startTime,endTime);
                if(timeDifference < 0){
                    isDurationSelectionOk=false;
                    Toast.makeText(this,"Negative Time Ingerval !!!", Toast.LENGTH_LONG).show();
                }else if(timeDifference > 7){
                    isDurationSelectionOk = false;
                    Toast.makeText(this,"Time Interval can not be more than 7 days", Toast.LENGTH_LONG).show();
                }else{
                    isDurationSelectionOk =true;
                }
                // Log.e("time difference","timediff: "+timeDifference);
                // Log.e("Start time ",startTime);
                Log.e("End time ",endTime);
            }
        }else{
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            switch (selectedCheckBox){
                case 1:
                    endTime = dt.format(calendar.getTime());
                    calendar.add(Calendar.HOUR_OF_DAY, -1);
                    startTime = dt.format(calendar.getTime());
                    break;
                case 2:
                    endTime = dt.format(calendar.getTime());
                    calendar.add(Calendar.HOUR_OF_DAY, -2);
                    startTime = dt.format(calendar.getTime());
                    break;
                case 3:
                    endTime = dt.format(calendar.getTime());
                    calendar.add(Calendar.HOUR_OF_DAY, -6);
                    startTime = dt.format(calendar.getTime());
                    break;
                case 4:
                    endTime = dt.format(calendar.getTime());
                    calendar.add(Calendar.HOUR_OF_DAY, -12);
                    startTime = dt.format(calendar.getTime());
                    break;
                case 5:
                    endTime = dt.format(calendar.getTime());
                    calendar.add(Calendar.HOUR_OF_DAY, -24);
                    startTime = dt.format(calendar.getTime());
                    break;
                case 6:
                    startTime = dt.format(TimeHelper.getStartTimeOfADay(calendar.getTime()));
                    endTime = dt.format(calendar.getTime());
                    break;
                case 7:
                    calendar.add(Calendar.DATE,-1);
                    startTime = dt.format(TimeHelper.getStartTimeOfADay(calendar.getTime()));
                    endTime = dt.format(TimeHelper.getEndTimOfADay(calendar.getTime()));
                    break;
                case 8:
                    endTime = dt.format(TimeHelper.getEndTimOfADay(calendar.getTime()));
                    calendar.add(Calendar.DATE,-7);
                    startTime = dt.format(TimeHelper.getStartTimeOfADay(calendar.getTime()));
                    break;
                default:
                    break;
            }
             Log.e("spinner Time ","StartTime:"+startTime+" endTime: "+endTime);

        }
        if (isDurationSelectionOk){
            mGlobals.startTime = startTime;
            mGlobals.endTime = endTime;
            Log.e("spinner Time ","StartTime:"+ mGlobals.startTime+" endTime: "+mGlobals.endTime);

            Intent returnIntent = new Intent();
            this.setResult(Activity.RESULT_OK, returnIntent);
            this.finish();
        }
    }
}

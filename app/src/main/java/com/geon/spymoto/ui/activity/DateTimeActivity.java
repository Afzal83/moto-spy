package com.geon.spymoto.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.geon.spymoto.AppGlobal;
import com.geon.spymoto.R;
import com.geon.spymoto.helper.TimeHelper;
import com.kunzisoft.switchdatetime.SwitchDateTimeDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.support.v4.app.DialogFragment.STYLE_NORMAL;

public class DateTimeActivity extends AppCompatActivity{

    CheckBox durationOneHour,duarationTwoHour,durationSixHOur,durationTwelveHour,durationTwentyFourHour
            ,durationTodya,durationYesterDay,customDuration;

    LinearLayout customDateTimePicekrContainer_LL , customDateTimeNoteselectedContainer_LL ;

    Button doneSelection;

    int selectedCheckBox = 0;
    String customStartDate = "";
    String customEndDate = "";
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
                    selectedCheckBox = 8;
                    customDuration.setChecked(true);
                    customDateTimePicekrContainer_LL.setVisibility(View.VISIBLE);
                    customDateTimeNoteselectedContainer_LL.setVisibility(View.GONE);
                }
                else {
                    customDuration.setChecked(false);
                    customDateTimePicekrContainer_LL.setVisibility(View.GONE);
                    customDateTimeNoteselectedContainer_LL.setVisibility(View.VISIBLE);
                }
                break;
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

        customDateTimePicekrContainer_LL= findViewById(R.id.custom_duration_clock_container_ll);
        customDateTimeNoteselectedContainer_LL = findViewById(R.id.custom_duration_image_ll);

        doneSelection = (Button) findViewById(R.id.done_selection);

        doneSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        customDateTimePicekrContainer_LL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 setDateTimePicker();
            }
        });
        customDateTimeNoteselectedContainer_LL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                 setDateTimePicker();
            }
        });

    }

    void setDateTimePicker(){
        SwitchDateTimeDialogFragment dateTimeDialogFragment = SwitchDateTimeDialogFragment.newInstance(
                "Title example",
                "OK",
                "Cancel"
        );
        //dateTimeDialogFragment.setStyle();
        dateTimeDialogFragment.setDefaultHourOfDay(15);
        dateTimeDialogFragment.setDefaultMinute(20);
        dateTimeDialogFragment.setDefaultDay(4);
        dateTimeDialogFragment.setDefaultMonth(Calendar.MARCH);
        dateTimeDialogFragment.setDefaultYear(2017);

        try {
            dateTimeDialogFragment.setSimpleDateMonthAndDayFormat(new SimpleDateFormat("dd MMMM", Locale.getDefault()));
        } catch (SwitchDateTimeDialogFragment.SimpleDateMonthAndDayFormatException e) {
            Log.e("dbug", e.getMessage());
        }

        // Set listener
        dateTimeDialogFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {
                // Date is get on positive button click
                // Do something
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                // Date is get on negative button click
            }
        });

        // Show
        dateTimeDialogFragment.show(DateTimeActivity.this.getSupportFragmentManager(), "dialog_time");
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

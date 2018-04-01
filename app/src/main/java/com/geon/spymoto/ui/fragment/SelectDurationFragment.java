package com.geon.spymoto.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.geon.spymoto.AppGlobal;
import com.geon.spymoto.R;
import com.geon.spymoto.helper.TimeHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Babu on 9/27/2016.
 */
public class SelectDurationFragment extends DialogFragment implements View.OnClickListener,TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener {
    int mNum;
    View mView;
    TextView selectedDurationFromSpinner;
    int pickerIdentifier=0;
    boolean isTimeCustom = false ;
    String spinnerDuration = "";
    String customStartTime = "";
    String customEndTime = "";
    String customStartDate = "";
    String customEndDate = "";


    Calendar now = Calendar.getInstance();

    AppGlobal mGlobals;


   CallFromSelectDurationFragment mCallback;
    public interface CallFromSelectDurationFragment {
        public void onMessageFormSelectDurationFragment(String message);
    }




    public static SelectDurationFragment newInstance(int num) {
        SelectDurationFragment f = new SelectDurationFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mNum = getArguments().getInt("num");
        setStyle(STYLE_NORMAL,0);
    }

    @Override
    public void onResume() {
        super.onResume();
//        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
 //       params.width = ViewGroup.LayoutParams.MATCH_PARENT;
 //       params.height = ViewGroup.LayoutParams.MATCH_PARENT;
  //      getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_select_duration,container,false);
        mGlobals = ((AppGlobal)getActivity().getApplicationContext());

        initView();
        createSelectStartTimeSpinner();

        return mView;
    }
    void initView(){
        selectedDurationFromSpinner = (TextView)mView.findViewById(R.id.selectedDuration);

        mView.findViewById(R.id.btn_selectStartTime_duration).setOnClickListener(this);
        mView.findViewById(R.id.btn_selectStartDate_duration).setOnClickListener(this);
        mView.findViewById(R.id.btn_selectEndDate_duration).setOnClickListener(this);
        mView.findViewById(R.id.btn_selectendtTime_duration).setOnClickListener(this);
        mView.findViewById(R.id.btn_done_duration).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_selectStartTime_duration:
                pickerIdentifier =1 ;
                TimePickerDialog stpd= TimePickerDialog.newInstance(
                        SelectDurationFragment.this,
                        now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(getActivity())
                );
                stpd.show(getActivity().getFragmentManager(), "startTimePicker");
                break;
            case R.id.btn_selectStartDate_duration:
                pickerIdentifier = 2;
                DatePickerDialog sdpd = DatePickerDialog.newInstance(
                        SelectDurationFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                sdpd.show(getActivity().getFragmentManager(), "startDatePicker");
                break;
            case R.id.btn_selectendtTime_duration:
                pickerIdentifier = 3;
                TimePickerDialog etpd= TimePickerDialog.newInstance(
                        SelectDurationFragment.this,
                        now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE),
                        DateFormat.is24HourFormat(getActivity())
                );
                etpd.show(getActivity().getFragmentManager(), "endTimePicker");
                break;
            case R.id.btn_selectEndDate_duration:
                pickerIdentifier= 4;
                DatePickerDialog edpd = DatePickerDialog.newInstance(
                        SelectDurationFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                edpd.show(getActivity().getFragmentManager(), "endtDatePicker");
                break;
            case R.id.btn_done_duration:
                processDuration();
                break;
            default:
                break;

        }
    }
    void createSelectStartTimeSpinner(){
        Spinner spinner = (Spinner)mView.findViewById(R.id.duration_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.select_duration_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinnerDuration = parentView.getItemAtPosition(position)+"";
                Log.e("selected spinner item",spinnerDuration);
                if(spinnerDuration.contains("Custom")){
                    isTimeCustom = true;
                    mView.findViewById(R.id.linerLayout_custom_picker_container).setVisibility(View.VISIBLE);
                }else{
                    isTimeCustom = false;
                    selectedDurationFromSpinner.setText( spinnerDuration +" Duration is Selected");
                    mView.findViewById(R.id.linerLayout_custom_picker_container).setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear +=1;
        String mDate = ""+year+"-"+monthOfYear+"-"+dayOfMonth;
        switch (pickerIdentifier){
            case 2:
                ((Button)mView.findViewById(R.id.btn_selectStartDate_duration)).setText(mDate);
                customStartDate = mDate;
                //Toast.makeText(getActivity().getApplicationContext(),"startSELECTED dATE : "+customStartDate, Toast.LENGTH_LONG).show();
                break;
            case 4:
                ((Button)mView.findViewById(R.id.btn_selectEndDate_duration)).setText(mDate);
                customEndDate = mDate;
                //Toast.makeText(getActivity().getApplicationContext(),"endSELECTED dATE : "+  customEndDate, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String mTime = ""+hourOfDay+":"+minute+":"+second;
        switch (pickerIdentifier){
            case 1:
                ((Button)mView.findViewById(R.id.btn_selectStartTime_duration)).setText(mTime);
                customStartTime = mTime;
               // Toast.makeText(getActivity().getApplicationContext(),"starTime : "+customStartTime , Toast.LENGTH_LONG).show();
                break;
            case 3:
                ((Button)mView.findViewById(R.id.btn_selectendtTime_duration)).setText(mTime);
                customEndTime = mTime;
               // Toast.makeText(getActivity().getApplicationContext(),"endTime : "+customEndTime, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
    void  processDuration(){
        boolean isDurationSelectionOk =true;
        String startTime = "";
        String endTime = "";
        if (isTimeCustom){
            if(customStartTime.isEmpty() || customStartTime.contentEquals("")){
                isDurationSelectionOk =false;
                Toast.makeText(getActivity().getApplicationContext(),"Please Select Start Time", Toast.LENGTH_LONG).show();
            }
            else if(customStartDate.isEmpty() || customStartDate.contentEquals("")){
                isDurationSelectionOk =false;
                Toast.makeText(getActivity().getApplicationContext(),"Please Select Start Date", Toast.LENGTH_LONG).show();
            }
            else if(customEndTime.isEmpty()|| customEndTime.contentEquals("")){
                isDurationSelectionOk =false;
                Toast.makeText(getActivity().getApplicationContext(),"Please Select End Time", Toast.LENGTH_LONG).show();
            }
            else if(customEndDate.isEmpty()|| customEndDate.contentEquals("")){
                isDurationSelectionOk =false;
                Toast.makeText(getActivity().getApplicationContext(),"Please Select End Date", Toast.LENGTH_LONG).show();
            }else{
                startTime = customStartDate+" "+customStartTime;
                endTime = customEndDate+ " " + customEndTime;
                float timeDifference = TimeHelper.timeDifference(startTime,endTime);
                if(timeDifference < 0){
                    isDurationSelectionOk=false;
                    Toast.makeText(getActivity().getApplicationContext(),"Negative Time Ingerval !!!", Toast.LENGTH_LONG).show();
                }else if(timeDifference > 7){
                    isDurationSelectionOk = false;
                    Toast.makeText(getActivity().getApplicationContext(),"Time Interval can not be more than 7 days", Toast.LENGTH_LONG).show();
                }else{

                }
               // Log.e("time difference","timediff: "+timeDifference);
               // Log.e("Start time ",startTime);
                Log.e("End time ",endTime);
            }
        }else{
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(spinnerDuration.contentEquals(getResources().getStringArray(R.array.select_duration_array)[0])){
                endTime = dt.format(calendar.getTime());
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                startTime = dt.format(calendar.getTime());
            }
            else if(spinnerDuration.contentEquals(getResources().getStringArray(R.array.select_duration_array)[1])){
                endTime = dt.format(calendar.getTime());
                calendar.add(Calendar.HOUR_OF_DAY, -2);
                startTime = dt.format(calendar.getTime());
            }
            else if(spinnerDuration.contentEquals(getResources().getStringArray(R.array.select_duration_array)[2])){
                endTime = dt.format(calendar.getTime());
                calendar.add(Calendar.HOUR_OF_DAY, -6);
                startTime = dt.format(calendar.getTime());
            }
            else if(spinnerDuration.contentEquals(getResources().getStringArray(R.array.select_duration_array)[3])){
                endTime = dt.format(calendar.getTime());
                calendar.add(Calendar.HOUR_OF_DAY, -12);
                startTime = dt.format(calendar.getTime());
            }
            else if(spinnerDuration.contentEquals(getResources().getStringArray(R.array.select_duration_array)[4])){
                endTime = dt.format(calendar.getTime());
                calendar.add(Calendar.HOUR_OF_DAY, -24);
                startTime = dt.format(calendar.getTime());
            }
            else if(spinnerDuration.contentEquals(getResources().getStringArray(R.array.select_duration_array)[5])){
                startTime = dt.format(TimeHelper.getStartTimeOfADay(calendar.getTime()));
                endTime = dt.format(calendar.getTime());
            }
            else if(spinnerDuration.contentEquals(getResources().getStringArray(R.array.select_duration_array)[6])){
                calendar.add(Calendar.DATE,-1);
                startTime = dt.format(TimeHelper.getStartTimeOfADay(calendar.getTime()));
                endTime = dt.format(TimeHelper.getEndTimOfADay(calendar.getTime()));
            }
            else if(spinnerDuration.contentEquals(getResources().getStringArray(R.array.select_duration_array)[7])){
                endTime = dt.format(TimeHelper.getEndTimOfADay(calendar.getTime()));
                calendar.add(Calendar.DATE,-7);
                startTime = dt.format(TimeHelper.getStartTimeOfADay(calendar.getTime()));
            }
           // Log.e("spinner Time ","StartTime:"+startTime+" endTime: "+endTime);

        }
        if (isDurationSelectionOk){
            mGlobals.startTime = startTime;
            mGlobals.endTime = endTime;
            Log.e("spinner Time ","StartTime:"+ mGlobals.startTime+" endTime: "+mGlobals.endTime);

            Intent returnIntent = new Intent();
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        }
    }
}

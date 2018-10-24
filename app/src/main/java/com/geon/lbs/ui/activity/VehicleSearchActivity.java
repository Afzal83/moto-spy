package com.geon.lbs.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.geon.lbs.AppGlobal;
import com.geon.lbs.R;
import com.geon.lbs.model.Vehicle;

import java.util.ArrayList;
import java.util.List;


public class VehicleSearchActivity extends AppCompatActivity {

    AppGlobal mGlobals;
    ListViewAdapter adapter;
    ListView searchableListView;
    EditText inputSearch;
    List<String> listForUserVehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_vehicle_search);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        mGlobals = ((AppGlobal)this.getApplicationContext());
        initViews();

        listForUserVehicles = new ArrayList<String>();
        for(Vehicle vehicle : mGlobals.geonVehicleList){
            listForUserVehicles.add(vehicle.getNumber_plate());
        }
        createSearchableListView(listForUserVehicles);

    }
    @Override
    public void onResume() {
        super.onResume();
        if(inputSearch.requestFocus()) {
            //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
    }

    void initViews(){
        searchableListView = (ListView)findViewById(R.id.listview_select_vehicle_fr);
        inputSearch = (EditText)findViewById(R.id.inputSearch_select_vehicle_fr);
        if(mGlobals.geonVehicleList.size() >1){
            ((TextView)findViewById(R.id.vehiclNumer_select_vehicle_fr)).setText("You have "+mGlobals.geonVehicleList.size()+" vehicles");
        }
        else{
            ((TextView)findViewById(R.id.vehiclNumer_select_vehicle_fr)).setText("You have "+mGlobals.geonVehicleList.size()+" vehicle");
        }
    }
    void createSearchableListView(final List<String> listItme) {

        if (listItme.isEmpty()) {
            return;
        }
        adapter = new ListViewAdapter(listItme);
        searchableListView.setAdapter(adapter);
        String recentSearch = inputSearch.getText().toString();
        adapter.filter(recentSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String recentSearch = inputSearch.getText().toString();
                adapter.filter(recentSearch);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }
    public class ListViewAdapter extends BaseAdapter {

        //Context mContext;
        LayoutInflater inflater;
        private List<String> vehicleListInAdapter = null;
        private ArrayList<String> tempVehicleList = null;
        ListViewAdapter(List<String> vehicleList) {
            //mContext = context;
            this.vehicleListInAdapter =  new ArrayList<>(vehicleList);
            this.tempVehicleList = new ArrayList<>(vehicleListInAdapter);
            inflater = LayoutInflater.from(VehicleSearchActivity.this);
        }

        @Override
        public int getCount() {
            return vehicleListInAdapter.size();
        }

        @Override
        public String getItem(int position) {
            return vehicleListInAdapter.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            if (view == null) {
                view = inflater.inflate( R.layout.list_item, null);
            }
            TextView v = (TextView) view.findViewById(R.id.list_item_name);
            v.setText(vehicleListInAdapter.get(position));
            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Log.e("selected___string",vehicleListInAdapter.get(position));
                    mGlobals.selectedVehicle = vehicleListInAdapter.get(position);
                    InputMethodManager imm = (InputMethodManager) VehicleSearchActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputSearch.getWindowToken(),0);

                    Intent returnIntent = new Intent();
                    VehicleSearchActivity.this.setResult(Activity.RESULT_OK, returnIntent);
                    VehicleSearchActivity.this.finish();
                }
            });
            return view;
        }
        void filter(String charText) {
            Log.e(".........inpute text",charText);

            vehicleListInAdapter.clear();
            if (charText.length() ==0) {
                Log.e(">>charLength","length"+charText.length());
                Log.e(">>lengtn of tempList","lentgh :"+tempVehicleList.size());
                vehicleListInAdapter.addAll(tempVehicleList);
            }
            else {
                for (String s : tempVehicleList) {
                    if (s.contains(charText)) {
                        vehicleListInAdapter.add(s);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

}

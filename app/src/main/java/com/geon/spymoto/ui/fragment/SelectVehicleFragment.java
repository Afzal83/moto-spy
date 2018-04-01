package com.geon.spymoto.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.geon.spymoto.AppGlobal;
import com.geon.spymoto.R;
import com.geon.spymoto.model.Vehicle;
import com.geon.spymoto.model.VehicleInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Babu on 6/17/2017.
 */

public class SelectVehicleFragment extends DialogFragment {

    int mNum;
    View mView;
    AppGlobal mGlobals;
    ListViewAdapter adapter;
    ListView searchableListView;
    EditText inputSearch;
    List<String> listForUserVehicles;

    CallSelectVehicleFragment mCallback;
    public interface CallSelectVehicleFragment {
        public void onMessageFormSelectVehicleFragment(String message);
    }

    public static SelectVehicleFragment newInstance(int num) {
        SelectVehicleFragment f = new SelectVehicleFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_select_vehicle, container, false);
        mGlobals = ((AppGlobal)getActivity().getApplicationContext());
        initViews();

        listForUserVehicles = new ArrayList<String>();
        for(Vehicle vehicle : mGlobals.geonVehicleList){
             listForUserVehicles.add(vehicle.getNumber_plate());
        }
        createSearchableListView(listForUserVehicles);
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(inputSearch.requestFocus()) {
            //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
        }
    }

    void initViews(){
        searchableListView = (ListView)mView.findViewById(R.id.listview_select_vehicle_fr);
        inputSearch = (EditText)mView.findViewById(R.id.inputSearch_select_vehicle_fr);
        if(mGlobals.geonVehicleList.size() >1){
            ((TextView)mView.findViewById(R.id.vehiclNumer_select_vehicle_fr)).setText("You have "+mGlobals.geonVehicleList.size()+" vehicles");
        }
        else{
            ((TextView)mView.findViewById(R.id.vehiclNumer_select_vehicle_fr)).setText("You have "+mGlobals.geonVehicleList.size()+" vehicle");
        }
    }
    void createSearchableListView(final List<String> listItme) {

        if (listItme.isEmpty()) {
            return;
        }
        adapter = new ListViewAdapter(getContext(),listItme);
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

        Context mContext;
        LayoutInflater inflater;
        private List<String> vehicleListInAdapter = null;
        private ArrayList<String> tempVehicleList = null;
        public ListViewAdapter(Context context, List<String> vehicleList) {
            mContext = context;
            this.vehicleListInAdapter =  new ArrayList<>(vehicleList);
            this.tempVehicleList = new ArrayList<>(vehicleListInAdapter);
            inflater = LayoutInflater.from(mContext);
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
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(inputSearch.getWindowToken(),0);

                    Intent returnIntent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                }
            });
            return view;
        }
        public void filter(String charText) {
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

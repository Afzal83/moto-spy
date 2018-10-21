package com.geon.lbs.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geon.lbs.AppGlobal;
import com.geon.lbs.R;

public class ProfileFragment extends Fragment {

    View mView;
    TextView userName,userCategory,userPhone,userEmail;
    AppGlobal appGlobal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_profile,container,false);
        appGlobal =((AppGlobal)getActivity().getApplicationContext());
        bindView();
        return mView;
    }
    void bindView(){
        userName = mView.findViewById(R.id.user_name);
        userCategory = mView.findViewById(R.id.user_category);
        userPhone = mView.findViewById(R.id.user_phone);
        userEmail = mView.findViewById(R.id.user_email);

        String userNameStr = appGlobal.userFirsName + " "+appGlobal.userLastName;
        String userCatStr = "Category : "+appGlobal.userCatagory;
        String userPhoneStr = "Phone : "+ appGlobal.userPhone;
        String userEmailStr = "Email : " + appGlobal.userEmail;
        userName.setText(userNameStr);
        userCategory.setText(userCatStr);
        userPhone.setText(userPhoneStr);
        userEmail.setText(userEmailStr);
    }
}

package com.tvisha.trooptime.activity.activity;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tvisha.trooptime.R;


public class RequestFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_requests, container, false);
        //apiService = ApiClient.getClient().create(ApiInterface.class);
        //initializeViews(rootView);
        return rootView;

    }

}
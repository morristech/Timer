package com.iec.dwx.timer.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iec.dwx.timer.R;

public class OtherWishesFragment extends Fragment{

    public static OtherWishesFragment newInstance() {
        return new OtherWishesFragment();
    }

    public OtherWishesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_other_wishes, container, false);
    }


}

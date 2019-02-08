package com.khoinguyen.caphekhoinguyen.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khoinguyen.caphekhoinguyen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhuHoFragment extends Fragment {


    public PhuHoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phu_ho, container, false);
    }

}

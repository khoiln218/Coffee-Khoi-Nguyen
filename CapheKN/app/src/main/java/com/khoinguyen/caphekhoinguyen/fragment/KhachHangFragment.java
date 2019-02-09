package com.khoinguyen.caphekhoinguyen.fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.adapter.DonHangAdapter;
import com.khoinguyen.caphekhoinguyen.adapter.KhachHangAdapter;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class KhachHangFragment extends Fragment {
    private static final String TAG = "KhachHangFragment";

    private List<KhachHang> mKhachHangs;
    private RecyclerView mRecyclerView;
    private KhachHangAdapter mAdapter;

    public KhachHangFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ban_hang, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themKhachHang();
            }
        });
        return view;
    }

    private void themKhachHang() {
        LogUtils.d(TAG, "Click them khach hang");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mAdapter) {
            mRecyclerView.setAdapter(mAdapter);
        } else {
            getKhachHangList();
        }
    }

    private void getKhachHangList() {
        mKhachHangs = getKhachHangs();
        mAdapter = new KhachHangAdapter(getActivity(), mKhachHangs);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<KhachHang> getKhachHangs() {
        List<KhachHang> khachHangs = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            khachHangs.add(new KhachHang(i, Utils.randomName(10)));
        return khachHangs;
    }
}

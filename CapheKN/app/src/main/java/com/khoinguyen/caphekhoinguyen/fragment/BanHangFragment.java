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
public class BanHangFragment extends Fragment {
    private static final String TAG = "BanHangFragment";

    private List<DonHang> mDonHangs;
    private RecyclerView mRecyclerView;
    private DonHangAdapter mAdapter;

    public BanHangFragment() {
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
                banHang();
            }
        });
        return view;
    }

    private void banHang() {
        LogUtils.d(TAG, "Click ban hang");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mAdapter) {
            mRecyclerView.setAdapter(mAdapter);
        } else {
            getOrderList();
        }
    }

    private void getOrderList() {
        mDonHangs = getDonHangs();
        mAdapter = new DonHangAdapter(getActivity(), mDonHangs);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<DonHang> getDonHangs() {
        KhachHang kh;
        SanPham sp;
        List<DonHang> donHangs = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            sp = new SanPham(i, Utils.randomName(10), 10000 + new Random().nextInt(5000));
            kh = new KhachHang(i, Utils.randomName(10));
            donHangs.add(new DonHang(i, System.currentTimeMillis(), getString(R.string.status_dang_xu_ly), kh, sp));
        }
        return donHangs;
    }
}

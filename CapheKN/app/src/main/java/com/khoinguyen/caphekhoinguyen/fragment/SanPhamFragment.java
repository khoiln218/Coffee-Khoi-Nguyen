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
import com.khoinguyen.caphekhoinguyen.adapter.SanPhamAdapter;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class SanPhamFragment extends Fragment {
    private static final String TAG = "SanPhamFragment";

    private List<SanPham> mSanPhams;
    private RecyclerView mRecyclerView;
    private SanPhamAdapter mAdapter;

    public SanPhamFragment() {
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
                themSanPham();
            }
        });
        return view;
    }

    private void themSanPham() {
        LogUtils.d(TAG, "Click them san pham");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mAdapter) {
            mRecyclerView.setAdapter(mAdapter);
        } else {
            getSanPhamList();
        }
    }

    private void getSanPhamList() {
        mSanPhams = getSanPhams();
        mAdapter = new SanPhamAdapter(getActivity(), mSanPhams);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<SanPham> getSanPhams() {
        List<SanPham> sanPhams = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            sanPhams.add(new SanPham(i, Utils.randomName(10), 5000 + new Random().nextInt(15000)));
        return sanPhams;
    }
}

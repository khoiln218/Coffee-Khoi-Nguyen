package com.khoinguyen.caphekhoinguyen.fragment;


import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class BanHangFragment extends Fragment {

    private OnOrderInteractionListener mListener;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderInteractionListener) {
            mListener = (OnOrderInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOrderInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        mAdapter = new DonHangAdapter(getActivity(), mDonHangs, mListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<DonHang> getDonHangs() {
        KhachHang kh1 = new KhachHang(1, "Nam");
        SanPham sp1 = new SanPham(1, "Bò húc", 20000);
        List<DonHang> donHangs = new ArrayList<>();
        for (int i = 0 ; i < 20 ; i++)
            donHangs.add(0, new DonHang(System.currentTimeMillis() + new Random().nextInt(), getString(R.string.status_dang_xu_ly), kh1, sp1));
        return donHangs;
    }

    private void banHang() {
        LogUtils.d("BanHangFragment", "Click ban hang");
    }

    public interface OnOrderInteractionListener {
        void onOrderItemSelected(DonHang item, int btnActionId);

        void onListFragmentInteraction(DonHang item);
    }

}

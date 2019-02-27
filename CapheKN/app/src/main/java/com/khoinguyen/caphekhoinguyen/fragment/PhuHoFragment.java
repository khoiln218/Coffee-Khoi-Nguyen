package com.khoinguyen.caphekhoinguyen.fragment;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.adapter.KhachHangAdapter;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.utils.Constants;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhuHoFragment extends Fragment {
    private KhachHangFragment.OnKhachHangInteractionListener mListener;

    private List<KhachHang> mKhachHangs;
    private RecyclerView mRecyclerView;
    private KhachHangAdapter mAdapter;
    private DBController dbController;

    public PhuHoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phu_ho, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvKhachHang);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        dbController = DBController.getInstance(getActivity());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mAdapter) {
            mRecyclerView.setAdapter(mAdapter);
        } else {
            getKhachHangs();
        }
    }

    private void getKhachHangs() {
        showLoading();
        new MyTask().execute();
    }

    private void showLoading() {
        Utils.showProgressDialog(getActivity());
    }

    private void hideLoading() {
        Utils.hideProgressDialog();
    }

    private long getTongTien(String id) {
        List<DonHang> donHangs = dbController.layDonHangHoanThanhTheoKhachHang(id);
        long tongTien = 0;
        for (DonHang donHang : donHangs) {
            tongTien += donHang.getTongTien(getActivity());
        }
        return tongTien;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TrangChuFragment.OnTrangChuInteractionListener) {
            mListener = (KhachHangFragment.OnKhachHangInteractionListener) context;
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

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            List<KhachHang> khachHangs = dbController.layDanhSachKhachHang();
            mKhachHangs = new ArrayList<>();
            for (KhachHang khachHang : khachHangs) {
                if (getTongTien(khachHang.getId()) > 0)
                    mKhachHangs.add(khachHang);
            }
            Collections.sort(mKhachHangs, new Comparator<KhachHang>() {
                @Override
                public int compare(KhachHang left, KhachHang right) {
                    return (int) (getTongTien(right.getId()) - getTongTien(left.getId()));
                }
            });
            mAdapter = new KhachHangAdapter(getActivity(), mKhachHangs, Constants.TRANG_THAI_HOAN_THANH, false, mListener);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.setAdapter(mAdapter);
            hideLoading();
        }
    }
}

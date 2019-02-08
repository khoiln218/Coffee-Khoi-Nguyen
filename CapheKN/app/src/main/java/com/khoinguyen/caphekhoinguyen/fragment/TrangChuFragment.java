package com.khoinguyen.caphekhoinguyen.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khoinguyen.caphekhoinguyen.R;

public class TrangChuFragment extends Fragment {
    private OnTrangChuInteractionListener mListener;

    public TrangChuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trang_chu, container, false);
    }

    public void banHang() {
        if (mListener != null)
            mListener.onBanHangClick();
    }

    public void sanPham() {
        if (mListener != null)
            mListener.onSanPhamClick();
    }

    public void congNo() {
        if (mListener != null)
            mListener.onCongNoClick();
    }

    public void phuHo() {
        if (mListener != null)
            mListener.onPhuHoClick();
    }

    public void baoCao() {
        if (mListener != null)
            mListener.onBaoCaoClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTrangChuInteractionListener) {
            mListener = (OnTrangChuInteractionListener) context;
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


    public interface OnTrangChuInteractionListener {
        void onBanHangClick();

        void onSanPhamClick();

        void onBaoCaoClick();

        void onCongNoClick();

        void onPhuHoClick();
    }
}

package com.khoinguyen.caphekhoinguyen.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);
        RelativeLayout layoutBanHang = view.findViewById(R.id.layout_ban_hang);
        layoutBanHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banHang();
            }
        });

        RelativeLayout layoutSanPham = view.findViewById(R.id.layout_san_pham);
        layoutSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sanPham();
            }
        });

        RelativeLayout layoutCongNo = view.findViewById(R.id.layout_cong_no);
        layoutCongNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                congNo();
            }
        });

        RelativeLayout layoutPhuHo = view.findViewById(R.id.layout_phu_ho);
        layoutPhuHo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phuHo();
            }
        });

        RelativeLayout layoutKhachHang = view.findViewById(R.id.layout_khach_hang);
        layoutKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                khachHang();
            }
        });

        RelativeLayout layoutBaoCao = view.findViewById(R.id.layout_bao_cao);
        layoutBaoCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                baoCao();
            }
        });
        return view;
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

    private void khachHang() {
        if (mListener != null)
            mListener.onKhachHangClick();
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

        void onKhachHangClick();
    }
}

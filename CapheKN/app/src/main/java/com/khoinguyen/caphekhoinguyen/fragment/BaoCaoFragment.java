package com.khoinguyen.caphekhoinguyen.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.model.DonHang;

import java.text.DecimalFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaoCaoFragment extends Fragment {
    private TextView tvDoanhThuNgay;
    private TextView tvDoanhThuTuan;
    private TextView tvDoanhThuThang;
    private TextView tvBanHangNgay;
    private TextView tvBanHangTuan;
    private TextView tvBanHangThang;
    private TextView tvThuNoNgay;
    private TextView tvThuNoTuan;
    private TextView tvThuNoThang;
    private DBController dbController;

    public BaoCaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bao_cao, container, false);
        tvBanHangNgay = (TextView) view.findViewById(R.id.tvBanHangNgay);
        tvBanHangTuan = (TextView) view.findViewById(R.id.tvBanHangTuan);
        tvBanHangThang = (TextView) view.findViewById(R.id.tvBanHangThang);
        tvDoanhThuNgay = (TextView) view.findViewById(R.id.tvDoanhThuNgay);
        tvDoanhThuTuan = (TextView) view.findViewById(R.id.tvDoanhThuTuan);
        tvDoanhThuThang = (TextView) view.findViewById(R.id.tvDoanhThuThang);
        tvThuNoNgay = (TextView) view.findViewById(R.id.tvThuNoNgay);
        tvThuNoTuan = (TextView) view.findViewById(R.id.tvThuNoTuan);
        tvThuNoThang = (TextView) view.findViewById(R.id.tvThuNoThang);

        dbController = new DBController(getActivity());
        baoCao();
        return view;
    }

    private void baoCao() {
        List<DonHang> donHangsTheoNgay = dbController.layDonHangTheoNgay(System.currentTimeMillis());
        String formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsTheoNgay));
        tvDoanhThuNgay.setText(formattedPrice);
        List<DonHang> donHangsHoanThanhTheoNgay = dbController.layDonHangHoanThanhTheoNgay(System.currentTimeMillis());
        formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsHoanThanhTheoNgay));
        tvBanHangNgay.setText(formattedPrice);
        List<DonHang> donHangsDangXuLyTheoNgay = dbController.layDonHangDangXuLyTheoNgay(System.currentTimeMillis());
        formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsDangXuLyTheoNgay));
        tvThuNoNgay.setText(formattedPrice);
        List<DonHang> donHangsTheoTuan = dbController.layDonHangTheoTuan(System.currentTimeMillis());
        formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsTheoTuan));
        tvDoanhThuTuan.setText(formattedPrice);
        List<DonHang> donHangsHoanThanhTheoTuan = dbController.layDonHangHoanThanhTheoTuan(System.currentTimeMillis());
        formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsHoanThanhTheoTuan));
        tvBanHangTuan.setText(formattedPrice);
        List<DonHang> donHangsDangXuLyTheoTuan = dbController.layDonHangDangXuLyTheoTuan(System.currentTimeMillis());
        formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsDangXuLyTheoTuan));
        tvThuNoTuan.setText(formattedPrice);
        List<DonHang> donHangsTheoThang = dbController.layDonHangTheoThang(System.currentTimeMillis());
        formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsTheoThang));
        tvDoanhThuThang.setText(formattedPrice);
        List<DonHang> donHangsHoanThanhTheoThang = dbController.layDonHangHoanThanhTheoThang(System.currentTimeMillis());
        formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsHoanThanhTheoThang));
        tvBanHangThang.setText(formattedPrice);
        List<DonHang> donHangsDangXuLyTheoThang = dbController.layDonHangDangXuLyTheoThang(System.currentTimeMillis());
        formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsDangXuLyTheoThang));
        tvThuNoThang.setText(formattedPrice);
    }

    private long getTongTien(List<DonHang> donHangs) {
        long tongTien = 0;
        for (DonHang donHang : donHangs) {
            tongTien += donHang.getTongTien();
        }
        return tongTien;
    }
}

package com.khoinguyen.caphekhoinguyen.fragment;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

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
    private PieChartView chartNgay;
    private PieChartView chartTuan;
    private PieChartView chartThang;

    private String mDoanhThuNgay;
    private String mBanHangNgay;
    private String mThuNoNgay;
    private String mDoanhThuTuan;
    private String mBanHangTuan;
    private String mThuNoTuan;
    private String mDoanhThuThang;
    private String mBanHangThang;
    private String mThuNoThang;

    private PieChartData mPieChartDataNgay;
    private PieChartData mPieChartDataTuan;
    private PieChartData mPieChartDataThang;

    public BaoCaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bao_cao, container, false);
        tvBanHangNgay = view.findViewById(R.id.tvBanHangNgay);
        tvBanHangTuan = view.findViewById(R.id.tvBanHangTuan);
        tvBanHangThang = view.findViewById(R.id.tvBanHangThang);
        tvDoanhThuNgay = view.findViewById(R.id.tvDoanhThuNgay);
        tvDoanhThuTuan = view.findViewById(R.id.tvDoanhThuTuan);
        tvDoanhThuThang = view.findViewById(R.id.tvDoanhThuThang);
        tvThuNoNgay = view.findViewById(R.id.tvThuNoNgay);
        tvThuNoTuan = view.findViewById(R.id.tvThuNoTuan);
        tvThuNoThang = view.findViewById(R.id.tvThuNoThang);
        chartNgay = view.findViewById(R.id.chartNgay);
        chartTuan = view.findViewById(R.id.chartTuan);
        chartThang = view.findViewById(R.id.chartThang);

        baoCao();
        return view;
    }

    private void baoCao() {
        showLoading();
        new MyTask().execute();
    }

    private void showLoading() {
        Utils.showProgressDialog(getActivity());
    }

    private void hideLoading() {
        Utils.hideProgressDialog();
    }

    private PieChartData getPieChartData(List<DonHang> donHangs) {
        Map<String, Integer> sanPhamData = new HashMap<>();
        for (DonHang donHang : donHangs) {
            List<String> idSanPhams = donHang.getIdSanPhams();
            for (String id : idSanPhams) {
                SanPham sanPham = DBController.getInstance(getActivity()).laySanPhamTheoId(id);
                Integer tong = sanPhamData.get(sanPham.getTenSP());
                sanPhamData.put(sanPham.getTenSP(), tong != null ? tong + 1 : 1);
            }
        }
        List<SliceValue> pieData = new ArrayList<>();
        for (String key : sanPhamData.keySet()) {
            int tong = sanPhamData.get(key);
            pieData.add(new SliceValue(tong, ColorGenerator.MATERIAL.getColor(key)).setLabel(key + ":" + tong));
        }

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(8);
        pieChartData.setHasCenterCircle(true).setCenterText1("Tỉ trọng các mặt hàng").setCenterText1FontSize(10).setCenterText1Color(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        return pieChartData;
    }

    private long getTongTien(List<DonHang> donHangs) {
        long tongTien = 0;
        for (DonHang donHang : donHangs) {
            tongTien += donHang.getTongTien(getActivity());
        }
        return tongTien;
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            List<DonHang> donHangsTheoNgay = DBController.getInstance(getActivity()).layDonHangTheoNgay(System.currentTimeMillis());
            mDoanhThuNgay = new DecimalFormat("##,##0").format(getTongTien(donHangsTheoNgay));
            mPieChartDataNgay = getPieChartData(donHangsTheoNgay);
            List<DonHang> donHangsHoanThanhTheoNgay = DBController.getInstance(getActivity()).layDonHangHoanThanhTheoNgay(System.currentTimeMillis());
            mBanHangNgay = new DecimalFormat("##,##0").format(getTongTien(donHangsHoanThanhTheoNgay));
            List<DonHang> donHangsDangXuLyTheoNgay = DBController.getInstance(getActivity()).layDonHangDangXuLyTheoNgay(System.currentTimeMillis());
            mThuNoNgay = new DecimalFormat("##,##0").format(getTongTien(donHangsDangXuLyTheoNgay));
            List<DonHang> donHangsTheoTuan = DBController.getInstance(getActivity()).layDonHangTheoTuan(System.currentTimeMillis());
            mDoanhThuTuan = new DecimalFormat("##,##0").format(getTongTien(donHangsTheoTuan));
            mPieChartDataTuan = getPieChartData(donHangsTheoTuan);
            List<DonHang> donHangsHoanThanhTheoTuan = DBController.getInstance(getActivity()).layDonHangHoanThanhTheoTuan(System.currentTimeMillis());
            mBanHangTuan = new DecimalFormat("##,##0").format(getTongTien(donHangsHoanThanhTheoTuan));
            List<DonHang> donHangsDangXuLyTheoTuan = DBController.getInstance(getActivity()).layDonHangDangXuLyTheoTuan(System.currentTimeMillis());
            mThuNoTuan = new DecimalFormat("##,##0").format(getTongTien(donHangsDangXuLyTheoTuan));
            List<DonHang> donHangsTheoThang = DBController.getInstance(getActivity()).layDonHangTheoThang(System.currentTimeMillis());
            mDoanhThuThang = new DecimalFormat("##,##0").format(getTongTien(donHangsTheoThang));
            mPieChartDataThang = getPieChartData(donHangsTheoThang);
            List<DonHang> donHangsHoanThanhTheoThang = DBController.getInstance(getActivity()).layDonHangHoanThanhTheoThang(System.currentTimeMillis());
            mBanHangThang = new DecimalFormat("##,##0").format(getTongTien(donHangsHoanThanhTheoThang));
            List<DonHang> donHangsDangXuLyTheoThang = DBController.getInstance(getActivity()).layDonHangDangXuLyTheoThang(System.currentTimeMillis());
            mThuNoThang = new DecimalFormat("##,##0").format(getTongTien(donHangsDangXuLyTheoThang));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            chartNgay.setPieChartData(mPieChartDataNgay);
            tvDoanhThuNgay.setText(mDoanhThuNgay);
            tvBanHangNgay.setText(mBanHangNgay);
            tvThuNoNgay.setText(mThuNoNgay);
            chartTuan.setPieChartData(mPieChartDataTuan);
            tvDoanhThuTuan.setText(mDoanhThuTuan);
            tvBanHangTuan.setText(mBanHangTuan);
            tvThuNoTuan.setText(mThuNoTuan);
            chartThang.setPieChartData(mPieChartDataThang);
            tvDoanhThuThang.setText(mDoanhThuThang);
            tvBanHangThang.setText(mBanHangThang);
            tvThuNoThang.setText(mThuNoThang);
            hideLoading();
        }
    }
}

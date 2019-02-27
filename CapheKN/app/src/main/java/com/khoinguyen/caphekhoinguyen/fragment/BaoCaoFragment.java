package com.khoinguyen.caphekhoinguyen.fragment;


import android.os.Bundle;
import android.os.Handler;
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
    private DBController dbController;

    public BaoCaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
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
        chartNgay = (PieChartView) view.findViewById(R.id.chartNgay);
        chartTuan = (PieChartView) view.findViewById(R.id.chartTuan);
        chartThang = (PieChartView) view.findViewById(R.id.chartThang);

        dbController = DBController.getInstance(getActivity());
        baoCao();
        return view;
    }

    private void baoCao() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                List<DonHang> donHangsTheoNgay = dbController.layDonHangTheoNgay(System.currentTimeMillis());
                initChart(chartNgay, donHangsTheoNgay);
                String formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsTheoNgay));
                tvDoanhThuNgay.setText(formattedPrice);
                List<DonHang> donHangsHoanThanhTheoNgay = dbController.layDonHangHoanThanhTheoNgay(System.currentTimeMillis());
                formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsHoanThanhTheoNgay));
                tvBanHangNgay.setText(formattedPrice);
                List<DonHang> donHangsDangXuLyTheoNgay = dbController.layDonHangDangXuLyTheoNgay(System.currentTimeMillis());
                formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsDangXuLyTheoNgay));
                tvThuNoNgay.setText(formattedPrice);
                List<DonHang> donHangsTheoTuan = dbController.layDonHangTheoTuan(System.currentTimeMillis());
                initChart(chartTuan, donHangsTheoTuan);
                formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsTheoTuan));
                tvDoanhThuTuan.setText(formattedPrice);
                List<DonHang> donHangsHoanThanhTheoTuan = dbController.layDonHangHoanThanhTheoTuan(System.currentTimeMillis());
                formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsHoanThanhTheoTuan));
                tvBanHangTuan.setText(formattedPrice);
                List<DonHang> donHangsDangXuLyTheoTuan = dbController.layDonHangDangXuLyTheoTuan(System.currentTimeMillis());
                formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsDangXuLyTheoTuan));
                tvThuNoTuan.setText(formattedPrice);
                List<DonHang> donHangsTheoThang = dbController.layDonHangTheoThang(System.currentTimeMillis());
                initChart(chartThang, donHangsTheoThang);
                formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsTheoThang));
                tvDoanhThuThang.setText(formattedPrice);
                List<DonHang> donHangsHoanThanhTheoThang = dbController.layDonHangHoanThanhTheoThang(System.currentTimeMillis());
                formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsHoanThanhTheoThang));
                tvBanHangThang.setText(formattedPrice);
                List<DonHang> donHangsDangXuLyTheoThang = dbController.layDonHangDangXuLyTheoThang(System.currentTimeMillis());
                formattedPrice = new DecimalFormat("##,##0").format(getTongTien(donHangsDangXuLyTheoThang));
                tvThuNoThang.setText(formattedPrice);
            }
        });
    }

    private void initChart(PieChartView chart, List<DonHang> donHangs) {
        Map<String, Integer> sanPhamData = new HashMap<>();
        for (DonHang donHang : donHangs) {
            List<String> idSanPhams = donHang.getIdSanPhams();
            for (String id : idSanPhams) {
                SanPham sanPham = dbController.laySanPhamTheoId(id);
                Integer tong = sanPhamData.get(sanPham.getTenSP());
                sanPhamData.put(sanPham.getTenSP(), tong != null ? tong.intValue() + 1 : 1);
            }
        }
        List<SliceValue> pieData = new ArrayList<>();
        for (String key : sanPhamData.keySet()) {
            int tong = sanPhamData.get(key).byteValue();
            pieData.add(new SliceValue(tong, ColorGenerator.MATERIAL.getColor(key)).setLabel(key + ":" + tong));
        }

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true).setValueLabelTextSize(10);
        pieChartData.setHasCenterCircle(true).setCenterText1("Tỉ trọng các mặt hàng").setCenterText1FontSize(10).setCenterText1Color(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
        chart.setPieChartData(pieChartData);
    }

    private long getTongTien(List<DonHang> donHangs) {
        long tongTien = 0;
        for (DonHang donHang : donHangs) {
            tongTien += donHang.getTongTien(getActivity());
        }
        return tongTien;
    }
}

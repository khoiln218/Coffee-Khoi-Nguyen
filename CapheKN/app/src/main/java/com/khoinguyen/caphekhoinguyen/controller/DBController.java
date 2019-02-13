package com.khoinguyen.caphekhoinguyen.controller;

import android.content.Context;

import com.khoinguyen.caphekhoinguyen.database.DonHangHandler;
import com.khoinguyen.caphekhoinguyen.database.KhachHangHandler;
import com.khoinguyen.caphekhoinguyen.database.SanPhamHandler;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;

import java.util.List;

public class DBController {
    private Context mContext;
    private DonHangHandler mDonHangHandler;
    private KhachHangHandler mKhachHangHandler;
    private SanPhamHandler mSanPhamHandler;

    public DBController(Context context) {
        this.mContext = context;
        mDonHangHandler = new DonHangHandler(context);
        mKhachHangHandler = new KhachHangHandler(context);
        mSanPhamHandler = new SanPhamHandler(context);
    }

    // Don hang
    public List<DonHang> layDanhSachDonHang() {
        return mDonHangHandler.getAllDonHang();
    }

    public List<DonHang> layDonHangDangXuLy() {
        return mDonHangHandler.getDonHangDangXuLy();
    }

    public List<DonHang> layDonHangDangXuLyTheoKhachHang(int idKhachHang) {
        return mDonHangHandler.getDonHangDangXuLyByKhachHang(idKhachHang);
    }

    public List<DonHang> layDonHangHoanThanhTheoKhachHang(int idKhachHang) {
        return mDonHangHandler.getDonHangHoanThanhByKhachHang(idKhachHang);
    }

    public List<DonHang> layDonHangTheoNgay(long time) {
        return mDonHangHandler.getDonHangByDate(time);
    }

    public List<DonHang> layDonHangTheoTuan(long time) {
        return mDonHangHandler.getDonHangByWeek(time);
    }

    public List<DonHang> layDonHangTheoThang(long time) {
        return mDonHangHandler.getDonHangByMonth(time);
    }

    public List<DonHang> layDonHangHoanThanhTheoNgay(long time) {
        return mDonHangHandler.layDonHangHoanThanhTheoNgay(time);
    }

    public List<DonHang> layDonHangDangXuLyTheoNgay(long time) {
        return mDonHangHandler.layDonHangDangXuLyTheoNgay(time);
    }

    public List<DonHang> layDonHangHoanThanhTheoTuan(long time) {
        return mDonHangHandler.layDonHangHoanThanhTheoTuan(time);
    }

    public List<DonHang> layDonHangDangXuLyTheoTuan(long time) {
        return mDonHangHandler.layDonHangDangXuLyTheoTuan(time);
    }

    public List<DonHang> layDonHangHoanThanhTheoThang(long time) {
        return mDonHangHandler.layDonHangHoanThanhTheoThang(time);
    }

    public List<DonHang> layDonHangDangXuLyTheoThang(long time) {
        return mDonHangHandler.layDonHangDangXuLyTheoThang(time);
    }

    public void themDonHang(DonHang donHang) {
        mDonHangHandler.insertDonHang(donHang);
    }

    public void capNhatDonHang(DonHang donHang) {
        mDonHangHandler.updateDonHang(donHang);
    }

    // Khach hang
    public List<KhachHang> layDanhSachKhachHang() {
        return mKhachHangHandler.getAllKhachHang();
    }

    public void themKhachHang(KhachHang khachHang) {
        mKhachHangHandler.insertKhachHang(khachHang);
    }

    public void capNhatKhachHang(KhachHang khachHang) {
        mKhachHangHandler.updateKhachHang(khachHang);
    }

    // San pham
    public List<SanPham> layDanhSachSanPham() {
        return mSanPhamHandler.getAllSanPham();
    }

    public void themSanPham(SanPham sanPham) {
        mSanPhamHandler.insertSanPham(sanPham);
    }

    public void xoaSanPham(int id) {
        mSanPhamHandler.deleteSanPham(id);
    }

    public void capNhatSanPham(SanPham sanPham) {
        mSanPhamHandler.updateSanPham(sanPham);
    }
}

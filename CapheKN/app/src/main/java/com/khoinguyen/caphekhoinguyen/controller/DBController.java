package com.khoinguyen.caphekhoinguyen.controller;

import android.content.Context;

import com.khoinguyen.caphekhoinguyen.database.DonHangHandler;
import com.khoinguyen.caphekhoinguyen.database.KhachHangHandler;
import com.khoinguyen.caphekhoinguyen.database.SanPhamHandler;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.realtimedatabase.RealtimeDatabaseController;

import java.util.List;

public class DBController {
    private static DBController INSTANCE = null;

    private Context mContext;
    private DonHangHandler mDonHangHandler;
    private KhachHangHandler mKhachHangHandler;
    private SanPhamHandler mSanPhamHandler;

    private DBController(Context context) {
        mContext = context;
        mDonHangHandler = DonHangHandler.getInstance(context);
        mKhachHangHandler = KhachHangHandler.getInstance(context);
        mSanPhamHandler = SanPhamHandler.getInstance(context);
    }

    public synchronized static DBController getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DBController(context);
        }
        return (INSTANCE);
    }

    // Don hang
    public List<DonHang> layDanhSachDonHang() {
        return mDonHangHandler.getAllDonHang();
    }

    public DonHang layDonHangTheoId(String idDonHang) {
        return mDonHangHandler.getDonHangById(idDonHang);
    }

    public List<DonHang> layDonHangDangXuLy() {
        return mDonHangHandler.getDonHangDangXuLy();
    }

    public List<DonHang> layDonHangDangXuLyTheoKhachHang(String idKhachHang) {
        return mDonHangHandler.getDonHangDangXuLyByKhachHang(idKhachHang);
    }

    public List<DonHang> layDonHangHoanThanhTheoKhachHang(String idKhachHang) {
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
        themDonHangDenDB(donHang);
        themDonHangDenServer(donHang);
    }

    public synchronized void themDonHangDenDB(DonHang donHang) {
        mDonHangHandler.insertOrUpdateDonHang(donHang);
    }

    public void themDonHangDenServer(DonHang donHang) {
        RealtimeDatabaseController.getInstance(mContext).themDonHang(donHang);
    }

    public void capNhatDonHang(DonHang donHang) {
        capNhatDonHangDenDB(donHang);
        capNhatDonHangDenServer(donHang);
    }

    public void capNhatDonHangDenDB(DonHang donHang) {
        mDonHangHandler.updateDonHang(donHang);
    }

    public void capNhatDonHangDenServer(DonHang donHang) {
        RealtimeDatabaseController.getInstance(mContext).capNhatDonHang(donHang);
    }

    // Khach hang
    public List<KhachHang> layDanhSachKhachHang() {
        return mKhachHangHandler.getAllKhachHang();
    }

    public KhachHang layKhachHangTheoId(String idKhachHang) {
        return mKhachHangHandler.getKhachHangById(idKhachHang);
    }

    public void themKhachHang(KhachHang khachHang) {
        themKhachHangDenDB(khachHang);
        themKhachHangDenServer(khachHang);
    }

    public synchronized void themKhachHangDenDB(KhachHang khachHang) {
        mKhachHangHandler.insertOrUpdateKhachHang(khachHang);
    }

    public void themKhachHangDenServer(KhachHang khachHang) {
        RealtimeDatabaseController.getInstance(mContext).themKhachHang(khachHang);
    }

    public void capNhatKhachHang(KhachHang khachHang) {
        capNhatKhachHangDenDB(khachHang);
        capNhatKhachHangDenServer(khachHang);
    }

    public void capNhatKhachHangDenDB(KhachHang khachHang) {
        mKhachHangHandler.updateKhachHang(khachHang);
    }

    public void capNhatKhachHangDenServer(KhachHang khachHang) {
        RealtimeDatabaseController.getInstance(mContext).capNhatKhachHang(khachHang);
    }

    // San pham
    public List<SanPham> layDanhSachSanPham() {
        return mSanPhamHandler.getAllSanPham();
    }

    public SanPham laySanPhamTheoId(String idSanPham) {
        return mSanPhamHandler.getSanPhamById(idSanPham);
    }

    public void themSanPham(SanPham sanPham) {
        themSanPhamDenDB(sanPham);
        themSanPhamDenServer(sanPham);
    }

    public synchronized void themSanPhamDenDB(SanPham sanPham) {
        mSanPhamHandler.insertOrUpdateSanPham(sanPham);
    }

    public void themSanPhamDenServer(SanPham sanPham) {
        RealtimeDatabaseController.getInstance(mContext).themSanPham(sanPham);
    }

    public void capNhatSanPham(SanPham sanPham) {
        capNhatSanPhamDenDB(sanPham);
        capNhatSanPhamDenServer(sanPham);
    }

    public void capNhatSanPhamDenDB(SanPham sanPham) {
        mSanPhamHandler.updateSanPham(sanPham);
    }

    public void capNhatSanPhamDenServer(SanPham sanPham) {
        RealtimeDatabaseController.getInstance(mContext).capNhatSanPham(sanPham);
    }

    public void taiDuLieuLenServer() {
        List<SanPham> sanPhams = layDanhSachSanPham();
        List<KhachHang> khachHangs = layDanhSachKhachHang();
        List<DonHang> donHangs = layDanhSachDonHang();

        RealtimeDatabaseController.getInstance(mContext).taiSanPhamLenServer(sanPhams);
        RealtimeDatabaseController.getInstance(mContext).taiKhachHangLenServer(khachHangs);
        RealtimeDatabaseController.getInstance(mContext).taiDonHangLenServer(donHangs);
    }
}

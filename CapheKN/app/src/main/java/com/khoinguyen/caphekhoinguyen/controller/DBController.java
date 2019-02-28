package com.khoinguyen.caphekhoinguyen.controller;

import android.annotation.SuppressLint;
import android.content.Context;

import com.khoinguyen.caphekhoinguyen.cache.DonHangCache;
import com.khoinguyen.caphekhoinguyen.cache.KhachHangCache;
import com.khoinguyen.caphekhoinguyen.cache.SanPhamCache;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.realtimedatabase.RealtimeDatabaseController;

import java.util.List;

public class DBController {
    @SuppressLint("StaticFieldLeak")
    private static DBController INSTANCE = null;

    private Context mContext;

    private DonHangCache donHangCache;
    private KhachHangCache khachHangCache;
    private SanPhamCache sanPhamCache;

    private DBController(Context context) {
        mContext = context;
        donHangCache = DonHangCache.getInstance(context);
        khachHangCache = KhachHangCache.getInstance();
        sanPhamCache = SanPhamCache.getInstance();
    }

    public synchronized static DBController getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DBController(context);
        }
        return (INSTANCE);
    }

    // Don hang
    public List<DonHang> layDanhSachDonHang() {
        return donHangCache.layDanhSachDonHang();
    }

    public DonHang layDonHangTheoId(String idDonHang) {
        return donHangCache.layDonHangTheoId(idDonHang);
    }

    public List<DonHang> layDonHangDangXuLy() {
        return donHangCache.layDonHangDangXuLy();
    }

    public List<DonHang> layDonHangDangXuLyTheoKhachHang(String idKhachHang) {
        return donHangCache.layDonHangDangXuLyTheoKhachHang(idKhachHang);
    }

    public List<DonHang> layDonHangHoanThanhTheoKhachHang(String idKhachHang) {
        return donHangCache.layDonHangHoanThanhTheoKhachHang(idKhachHang);
    }

    public List<DonHang> layDonHangTheoNgay(long time) {
        return donHangCache.layDonHangTheoNgay(time);
    }

    public List<DonHang> layDonHangTheoTuan(long time) {
        return donHangCache.layDonHangTheoTuan(time);
    }

    public List<DonHang> layDonHangTheoThang(long time) {
        return donHangCache.layDonHangTheoThang(time);
    }

    public List<DonHang> layDonHangHoanThanhTheoNgay(long time) {
        return donHangCache.layDonHangHoanThanhTheoNgay(time);
    }

    public List<DonHang> layDonHangDangXuLyTheoNgay(long time) {
        return donHangCache.layDonHangDangXuLyTheoNgay(time);
    }

    public List<DonHang> layDonHangHoanThanhTheoTuan(long time) {
        return donHangCache.layDonHangHoanThanhTheoTuan(time);
    }

    public List<DonHang> layDonHangDangXuLyTheoTuan(long time) {
        return donHangCache.layDonHangDangXuLyTheoTuan(time);
    }

    public List<DonHang> layDonHangHoanThanhTheoThang(long time) {
        return donHangCache.layDonHangHoanThanhTheoThang(time);
    }

    public List<DonHang> layDonHangDangXuLyTheoThang(long time) {
        return donHangCache.layDonHangDangXuLyTheoThang(time);
    }

    public void themDonHang(DonHang donHang) {
        themDonHangDenServer(donHang);
    }

    public synchronized void capNhatHoacThemDonHangDenCache(DonHang donHang) {
        donHangCache.capNhatHoacThemDonHang(donHang);
    }

    public void themDonHangDenServer(DonHang donHang) {
        RealtimeDatabaseController.getInstance(mContext).themDonHang(donHang);
    }

    public void capNhatDonHang(DonHang donHang) {
        capNhatDonHangDenServer(donHang);
    }

    public synchronized void capNhatDonHangDenCache(DonHang donHang) {
        donHangCache.capNhatDonHang(donHang);
    }

    public void capNhatDonHangDenServer(DonHang donHang) {
        RealtimeDatabaseController.getInstance(mContext).capNhatDonHang(donHang);
    }

    // Khach hang
    public List<KhachHang> layDanhSachKhachHang() {
        return khachHangCache.layDanhSachKhachHang();
    }

    public KhachHang layKhachHangTheoId(String idKhachHang) {
        return khachHangCache.layKhachHangTheoId(idKhachHang);
    }

    public void themKhachHang(KhachHang khachHang) {
        themKhachHangDenServer(khachHang);
    }

    public synchronized void capNhatHoacThemKhachHangDenCache(KhachHang khachHang) {
        khachHangCache.capNhatHoacThemKhachHang(khachHang);
    }

    public void themKhachHangDenServer(KhachHang khachHang) {
        RealtimeDatabaseController.getInstance(mContext).themKhachHang(khachHang);
    }

    public void capNhatKhachHang(KhachHang khachHang) {
        capNhatKhachHangDenServer(khachHang);
    }

    public synchronized void capNhatKhachHangDenCache(KhachHang khachHang) {
        khachHangCache.capNhatKhachHang(khachHang);
    }

    public void capNhatKhachHangDenServer(KhachHang khachHang) {
        RealtimeDatabaseController.getInstance(mContext).capNhatKhachHang(khachHang);
    }

    // San pham
    public List<SanPham> layDanhSachSanPham() {
        return sanPhamCache.layDanhSachSanPham();
    }

    public SanPham laySanPhamTheoId(String idSanPham) {
        return sanPhamCache.laySanPhamTheoId(idSanPham);
    }

    public void themSanPham(SanPham sanPham) {
        themSanPhamDenServer(sanPham);
    }

    public synchronized void capNhatHoacThemSanPhamDenCache(SanPham sanPham) {
        sanPhamCache.capNhatHoacThemSanPham(sanPham);
    }

    public void themSanPhamDenServer(SanPham sanPham) {
        RealtimeDatabaseController.getInstance(mContext).themSanPham(sanPham);
    }

    public void capNhatSanPham(SanPham sanPham) {
        capNhatSanPhamDenServer(sanPham);
    }

    public synchronized void capNhatSanPhamDenCache(SanPham sanPham) {
        sanPhamCache.capNhatSanPham(sanPham);
    }

    public void capNhatSanPhamDenServer(SanPham sanPham) {
        RealtimeDatabaseController.getInstance(mContext).capNhatSanPham(sanPham);
    }

    public void taiDuLieuLenServer() {
        List<DonHang> donHangs = layDanhSachDonHang();
        List<KhachHang> khachHangs = layDanhSachKhachHang();
        List<SanPham> sanPhams = layDanhSachSanPham();

        RealtimeDatabaseController.getInstance(mContext).taiDonHangLenServer(donHangs);
        RealtimeDatabaseController.getInstance(mContext).taiKhachHangLenServer(khachHangs);
        RealtimeDatabaseController.getInstance(mContext).taiSanPhamLenServer(sanPhams);
    }
}

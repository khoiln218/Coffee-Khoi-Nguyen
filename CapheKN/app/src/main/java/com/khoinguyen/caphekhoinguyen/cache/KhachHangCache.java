package com.khoinguyen.caphekhoinguyen.cache;

import android.text.TextUtils;

import com.khoinguyen.caphekhoinguyen.model.KhachHang;

import java.util.ArrayList;
import java.util.List;

public class KhachHangCache {
    private static KhachHangCache INSTANCE = null;

    private static final List<KhachHang> khachHangs = new ArrayList<>();

    private KhachHangCache() {
    }

    public synchronized static KhachHangCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new KhachHangCache();
        }
        return (INSTANCE);
    }

    public List<KhachHang> layDanhSachKhachHang() {
        return khachHangs;
    }

    public KhachHang layKhachHangTheoId(String idKhachHang) {
        for (KhachHang khachHang : khachHangs)
            if (TextUtils.equals(khachHang.getId(), idKhachHang))
                return khachHang;
        return null;
    }

    public void capNhatHoacThemKhachHang(KhachHang khachHang) {
        if (layKhachHangTheoId(khachHang.getId()) != null)
            capNhatKhachHang(khachHang);
        else {
            for (int i = 0; i < khachHangs.size(); i++) {
                if (khachHang.getTenKH().compareTo(khachHangs.get(i).getTenKH()) <= 0) {
                    khachHangs.add(i, khachHang);
                    return;
                }
            }
            khachHangs.add(khachHang);
        }
    }

    public void capNhatKhachHang(KhachHang khachHang) {
        for (int i = 0; i < khachHangs.size(); i++) {
            if (TextUtils.equals(khachHang.getId(), khachHangs.get(i).getId())) {
                khachHangs.set(i, khachHang);
                return;
            }
        }
    }
}

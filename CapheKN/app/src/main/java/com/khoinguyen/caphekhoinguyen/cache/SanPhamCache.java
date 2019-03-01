package com.khoinguyen.caphekhoinguyen.cache;

import android.text.TextUtils;

import com.khoinguyen.caphekhoinguyen.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class SanPhamCache {
    private static SanPhamCache INSTANCE = null;

    private static List<SanPham> sanPhams = new ArrayList<>();

    private SanPhamCache() {
    }

    public synchronized static SanPhamCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SanPhamCache();
        }
        return (INSTANCE);
    }

    public List<SanPham> layDanhSachSanPham() {
        return sanPhams;
    }

    public SanPham laySanPhamTheoId(String idSanPham) {
        for (SanPham sanPham : sanPhams)
            if (TextUtils.equals(sanPham.getId(), idSanPham))
                return sanPham;
        return null;
    }

    public void capNhatHoacThemSanPham(SanPham sanPham) {
        if (laySanPhamTheoId(sanPham.getId()) != null)
            capNhatSanPham(sanPham);
        else {
            for (int i = 0; i < sanPhams.size(); i++) {
                if (sanPham.getTenSP().compareTo(sanPhams.get(i).getTenSP()) <= 0) {
                    sanPhams.add(i, sanPham);
                    return;
                }
            }
            sanPhams.add(sanPham);
        }
    }

    public void capNhatSanPham(SanPham sanPham) {
        for (int i = 0; i < sanPhams.size(); i++) {
            if (TextUtils.equals(sanPham.getId(), sanPhams.get(i).getId())) {
                sanPhams.set(i, sanPham);
                return;
            }
        }
    }

    public void clear() {
        sanPhams.clear();
    }
}

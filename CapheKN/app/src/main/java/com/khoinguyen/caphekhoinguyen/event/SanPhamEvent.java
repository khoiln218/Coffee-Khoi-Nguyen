package com.khoinguyen.caphekhoinguyen.event;

import com.khoinguyen.caphekhoinguyen.model.SanPham;

public class SanPhamEvent {
    private SanPham sanPham;

    public SanPhamEvent() {
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
}

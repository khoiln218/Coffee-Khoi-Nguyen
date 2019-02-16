package com.khoinguyen.caphekhoinguyen.event;

import com.khoinguyen.caphekhoinguyen.model.KhachHang;

public class KhachHangEvent {
    private KhachHang khachHang;

    public KhachHangEvent() {
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }
}

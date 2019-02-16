package com.khoinguyen.caphekhoinguyen.event;

import com.khoinguyen.caphekhoinguyen.model.DonHang;

public class DonHangEvent {
    private DonHang donHang;

    public DonHangEvent() {
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }
}

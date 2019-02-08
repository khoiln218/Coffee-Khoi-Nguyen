package com.khoinguyen.caphekhoinguyen.model;

public class SanPham {
    int id;
    String tenSP;
    double donGia;

    public SanPham() {

    }

    public SanPham(int id, String tenSP, double donGia) {
        this.id = id;
        this.tenSP = tenSP;
        this.donGia = donGia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }
}

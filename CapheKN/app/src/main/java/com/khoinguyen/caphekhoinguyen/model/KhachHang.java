package com.khoinguyen.caphekhoinguyen.model;

public class KhachHang {
    private int maKH;
    private String tenKH;
    private String sDT;

    public KhachHang(int maKH, String tenKH, String sDT) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sDT = sDT;
    }

    public KhachHang(int maKH, String tenKH) {
        this.maKH = maKH;
        this.tenKH = tenKH;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getsDT() {
        return sDT;
    }

    public void setsDT(String sDT) {
        this.sDT = sDT;
    }
}

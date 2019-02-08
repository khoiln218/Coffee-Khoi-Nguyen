package com.khoinguyen.caphekhoinguyen.model;

public class KhachHang {
    private int id;
    private String tenKH;
    private String sDT;

    public KhachHang() {
    }

    public KhachHang(int id, String tenKH, String sDT) {
        this.id = id;
        this.tenKH = tenKH;
        this.sDT = sDT;
    }

    public KhachHang(int id, String tenKH) {
        this.id = id;
        this.tenKH = tenKH;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSDT() {
        return sDT;
    }

    public void setSDT(String sDT) {
        this.sDT = sDT;
    }
}

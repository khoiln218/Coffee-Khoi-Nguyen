package com.khoinguyen.caphekhoinguyen.model;

public class DonHang {
    private int id;
    private long thoiGianTao;
    private String trangThai;
    private KhachHang khachHang;
    private SanPham sanPham;

    public DonHang() {

    }

    public DonHang(int id, long thoiGianTao, String trangThai, KhachHang khachHang, SanPham sanPham) {
        this.id = id;
        this.thoiGianTao = thoiGianTao;
        this.trangThai = trangThai;
        this.khachHang = khachHang;
        this.sanPham = sanPham;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getThoiGianTao() {
        return thoiGianTao;
    }

    public void setThoiGianTao(long thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }
}

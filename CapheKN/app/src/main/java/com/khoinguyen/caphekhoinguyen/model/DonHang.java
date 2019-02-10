package com.khoinguyen.caphekhoinguyen.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khoinguyen.caphekhoinguyen.database.SanPhamHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DonHang {
    private int id;
    private long thoiGianTao;
    private String trangThai;
    private KhachHang khachHang;
    private List<SanPham> sanPhams;

    public DonHang() {

    }

    public DonHang(int id, long thoiGianTao, String trangThai, KhachHang khachHang, List<SanPham> sanPhams) {
        this.id = id;
        this.thoiGianTao = thoiGianTao;
        this.trangThai = trangThai;
        this.khachHang = khachHang;
        this.sanPhams = sanPhams;
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

    public List<SanPham> getSanPhams() {
        return sanPhams;
    }

    public void setSanPhams(List<SanPham> sanPhams) {
        this.sanPhams = sanPhams;
    }

    public void addSanPham(SanPham sanPham) {
        if (this.sanPhams == null)
            sanPhams = new ArrayList<>();
        sanPhams.add(sanPham);
    }

    public String convertSanPhamsToJsonString() {
        List<Integer> dsSanPhams = new ArrayList<>();
        for (SanPham sanPham : sanPhams) {
            dsSanPhams.add(sanPham.getId());
        }
        Gson gson = new Gson();
        return gson.toJson(dsSanPhams);
    }

    public void setSanPhamsFromJsonString(String jsonString, SanPhamHandler sanPhamHandler) {
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        Gson gson = new Gson();

        List<Integer> dsSanPhams = gson.fromJson(jsonString, type);
        sanPhams = new ArrayList<>();
        for (Integer id : dsSanPhams) {
            sanPhams.add(sanPhamHandler.getSanPhamById(id));
        }
    }
}

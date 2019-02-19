package com.khoinguyen.caphekhoinguyen.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.khoinguyen.caphekhoinguyen.controller.DBController;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DonHang {
    private String id;
    private long thoiGianTao;
    private String trangThai;
    private String idKhachHang;
    private List<String> idSanPhams;

    public DonHang() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public List<String> getIdSanPhams() {
        return idSanPhams;
    }

    public void setIdSanPhams(List<String> idSanPhams) {
        this.idSanPhams = idSanPhams;
    }

    public void addSanPham(SanPham sanPham) {
        if (this.idSanPhams == null)
            idSanPhams = new ArrayList<>();
        idSanPhams.add(sanPham.getId());
    }

    public String convertSanPhamsToJsonString() {
        Gson gson = new Gson();
        return gson.toJson(idSanPhams);
    }

    public void setIdSanPhamsFromJsonString(String jsonString) {
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        Gson gson = new Gson();

        idSanPhams = gson.fromJson(jsonString, type);
    }

    public long getTongTien(Context context) {
        long tongTien = 0;
        for (String id : idSanPhams) {
            tongTien += DBController.getInstance(context).laySanPhamTheoId(id).getDonGia();
        }
        return tongTien;
    }
}

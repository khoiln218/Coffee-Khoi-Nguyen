package com.khoinguyen.caphekhoinguyen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class DonHangHandler {

    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private ContentValues values;

    private SanPhamHandler sanPhamHandler;
    private KhachHangHandler khachHangHandler;

    public DonHangHandler(Context context) {
        this.dbHelper = new DBHelper(context);
        sanPhamHandler = new SanPhamHandler(context);
        khachHangHandler = new KhachHangHandler(context);
    }

    public void insertDonHang(DonHang donHang) {
        db = dbHelper.getWritableDatabase();

        values = new ContentValues();
        values.put(DBConstant.DON_HANG_THOI_GIAN_TAO, donHang.getThoiGianTao());
        values.put(DBConstant.DON_HANG_TRANG_THAI, donHang.getTrangThai());
        values.put(DBConstant.DON_HANG_MA_KHACH_HANG, donHang != null ? donHang.getKhachHang().getId() : null);
        values.put(DBConstant.DON_HANG_MA_SAN_PHAM, donHang != null ? donHang.getSanPham().getId() : null);

        db.insert(DBConstant.TABLE_NAME_DON_HANG, null, values);
        values = null;
        db.close();
    }

    public long updateDonHang(DonHang donHang) {
        db = dbHelper.getWritableDatabase();

        values = new ContentValues();
        values.put(DBConstant.DON_HANG_THOI_GIAN_TAO, donHang.getThoiGianTao());
        values.put(DBConstant.DON_HANG_TRANG_THAI, donHang.getTrangThai());
        values.put(DBConstant.DON_HANG_MA_KHACH_HANG, donHang != null ? donHang.getKhachHang().getId() : null);
        values.put(DBConstant.DON_HANG_MA_SAN_PHAM, donHang != null ? donHang.getSanPham().getId() : null);

        long rowUpdate = db.update(DBConstant.TABLE_NAME_DON_HANG, values, DBConstant.DON_HANG_ID + "=?", new String[]{String.valueOf(donHang.getId())});
        values = null;
        db.close();
        return rowUpdate;
    }

    /*
    Select a don hang by ID
     */

    public DonHang getDonHangById(int id) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstant.TABLE_NAME_DON_HANG, new String[]{DBConstant.DON_HANG_ID,
                        DBConstant.DON_HANG_THOI_GIAN_TAO, DBConstant.DON_HANG_TRANG_THAI, DBConstant.DON_HANG_MA_KHACH_HANG, DBConstant.DON_HANG_MA_SAN_PHAM}, DBConstant.DON_HANG_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DonHang donHang = new DonHang();
        donHang.setId(cursor.getInt(0));
        donHang.setThoiGianTao(cursor.getLong(1));
        donHang.setTrangThai(cursor.getString(2));

        String maKH = cursor.getString(3);
        if (!TextUtils.isEmpty(maKH)) {
            KhachHang khachHang = khachHangHandler.getKhachHangById(Integer.valueOf(maKH));
            donHang.setKhachHang(khachHang);
        }

        String maSP = cursor.getString(4);
        if (!TextUtils.isEmpty(maSP)) {
            SanPham sanPham = sanPhamHandler.getSanPhamById(Integer.valueOf(maSP));
            donHang.setSanPham(sanPham);
        }
        cursor.close();
        db.close();
        return donHang;
    }

    /*
     Getting All don hang
      */

    public List<DonHang> getAllDonHang() {
        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG;

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getInt(0));
                donHang.setThoiGianTao(cursor.getLong(1));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(Integer.valueOf(maKH));
                    donHang.setKhachHang(khachHang);
                }

                String maSP = cursor.getString(4);
                if (!TextUtils.isEmpty(maSP)) {
                    SanPham sanPham = sanPhamHandler.getSanPhamById(Integer.valueOf(maSP));
                    donHang.setSanPham(sanPham);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
    }

    /*
   Delete a don hang by ID
    */
    public void deleteDonHang(DonHang donHang) {
        db = dbHelper.getWritableDatabase();
        db.delete(DBConstant.TABLE_NAME_DON_HANG, DBConstant.DON_HANG_ID + " = ?",
                new String[]{String.valueOf(donHang.getId())});
        db.close();
    }

    /*
    Get Count don hang in Table don hang
     */
    public int getDonHangsCount() {
        String countQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();
        db.close();
        return rowCount;
    }
}

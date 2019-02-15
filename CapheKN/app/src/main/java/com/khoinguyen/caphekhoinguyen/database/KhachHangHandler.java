package com.khoinguyen.caphekhoinguyen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.khoinguyen.caphekhoinguyen.model.KhachHang;

import java.util.ArrayList;
import java.util.List;

public class KhachHangHandler {

    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private ContentValues values;

    public KhachHangHandler(Context context) {
        this.dbHelper = DBHelper.getInstance(context);
    }

    public void insertKhachHang(KhachHang khachHang) {
        db = dbHelper.getWritableDatabase();

        values = new ContentValues();
        values.put(DBConstant.KHACH_HANG_ID, khachHang.getId());
        values.put(DBConstant.KHACH_HANG_TEN, khachHang.getTenKH());
        values.put(DBConstant.KHACH_HANG_SDT, khachHang.getSDT());

        db.insert(DBConstant.TABLE_NAME_KHACH_HANG, null, values);
        values = null;
        db.close();
    }

    public long updateKhachHang(KhachHang khachHang) {
        db = dbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put(DBConstant.KHACH_HANG_TEN, khachHang.getTenKH());
        values.put(DBConstant.KHACH_HANG_SDT, khachHang.getSDT());

        long rowUpdate = db.update(DBConstant.TABLE_NAME_KHACH_HANG, values, DBConstant.KHACH_HANG_ID + "=?", new String[]{String.valueOf(khachHang.getId())});
        values = null;
        return rowUpdate;
    }

    public KhachHang getKhachHangById(String id) {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstant.TABLE_NAME_KHACH_HANG, new String[]{DBConstant.KHACH_HANG_ID,
                        DBConstant.KHACH_HANG_TEN, DBConstant.KHACH_HANG_SDT}, DBConstant.KHACH_HANG_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        KhachHang khachHang = new KhachHang();
        khachHang.setId(id);
        khachHang.setTenKH(cursor.getString(1));
        khachHang.setSDT(cursor.getString(2));
        cursor.close();
        db.close();
        return khachHang;
    }

    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> khachHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_KHACH_HANG;

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                KhachHang khachHang = new KhachHang();
                khachHang.setId(cursor.getString(0));
                khachHang.setTenKH(cursor.getString(1));
                khachHang.setSDT(cursor.getString(2));
                khachHangs.add(khachHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return khachHangs;
    }

    public void deleteKhachHang(String id) {
        db = dbHelper.getWritableDatabase();
        db.delete(DBConstant.TABLE_NAME_KHACH_HANG, DBConstant.KHACH_HANG_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public int getKhachHangsCount() {
        String countQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_KHACH_HANG;
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        cursor.close();
        db.close();
        return rowCount;
    }
}


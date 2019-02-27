package com.khoinguyen.caphekhoinguyen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DonHangHandler {
    private final static String TAG = "DonHangHandler";

    private static DonHangHandler INSTANCE = null;

    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private ContentValues values;
    private Context context;

    private DonHangHandler(Context context) {
        this.dbHelper = DBHelper.getInstance(context);
        this.context = context;
    }

    public static DonHangHandler getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DonHangHandler(context);
        }
        return (INSTANCE);
    }

    public void insertOrUpdateDonHang(DonHang donHang) {
        if (checkExits(donHang.getId())) {
            updateDonHang(donHang);
        } else {
            db = dbHelper.getWritableDatabase();

            values = new ContentValues();
            values.put(DBConstant.DON_HANG_ID, donHang.getId());
            values.put(DBConstant.DON_HANG_THOI_GIAN_TAO, donHang.getThoiGianTao());
            values.put(DBConstant.DON_HANG_TRANG_THAI, donHang.getTrangThai());
            values.put(DBConstant.DON_HANG_MA_KHACH_HANG, donHang.getIdKhachHang());
            values.put(DBConstant.DON_HANG_SAN_PHAM, donHang.convertSanPhamsToJsonString());

            db.insert(DBConstant.TABLE_NAME_DON_HANG, null, values);
            values = null;
            db.close();
        }
    }

    public long updateDonHang(DonHang donHang) {
        db = dbHelper.getWritableDatabase();

        values = new ContentValues();
        values.put(DBConstant.DON_HANG_THOI_GIAN_TAO, donHang.getThoiGianTao());
        values.put(DBConstant.DON_HANG_TRANG_THAI, donHang.getTrangThai());
        values.put(DBConstant.DON_HANG_MA_KHACH_HANG, donHang.getIdKhachHang());
        values.put(DBConstant.DON_HANG_SAN_PHAM, donHang.convertSanPhamsToJsonString());

        long rowUpdate = db.update(DBConstant.TABLE_NAME_DON_HANG, values, DBConstant.DON_HANG_ID + "=?", new String[]{String.valueOf(donHang.getId())});
        values = null;
        db.close();
        return rowUpdate;
    }

    /*
    Select a don hang by ID
     */

    public DonHang getDonHangById(String id) {
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_ID + " = " + "'" + id + "'";

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            DonHang donHang = new DonHang();
            donHang.setId(cursor.getString(0));
            donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
            donHang.setTrangThai(cursor.getString(2));
            donHang.setIdKhachHang(cursor.getString(3));
            donHang.setIdSanPhamsFromJsonString(cursor.getString(4));
            cursor.close();
            db.close();
            return donHang;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    /*
     Getting All don hang
      */

    public List<DonHang> getAllDonHang() {
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        return excQuery(selectQuery);
    }

    /*
   Delete a don hang by ID
    */
    public void deleteDonHang(int id) {
        db = dbHelper.getWritableDatabase();
        db.delete(DBConstant.TABLE_NAME_DON_HANG, DBConstant.DON_HANG_ID + " = ?",
                new String[]{String.valueOf(id)});
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

    public List<DonHang> getDonHangDangXuLy() {
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_dang_xu_ly) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        return excQuery(selectQuery);
    }

    public List<DonHang> getDonHangDangXuLyByKhachHang(String idKhachHang) {
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_MA_KHACH_HANG + " = " + "'" + idKhachHang + "'"
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_dang_xu_ly) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        return excQuery(selectQuery);
    }

    public List<DonHang> getDonHangHoanThanhByKhachHang(String idKhachHang) {
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_MA_KHACH_HANG + " = " + "'" + idKhachHang + "'"
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_hoan_thanh) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        return excQuery(selectQuery);
    }

    public List<DonHang> getDonHangByDate(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, day, 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " != " + "'" + context.getString(R.string.status_da_huy) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "getDonHangByDate: " + selectQuery);

        return excQuery(selectQuery);
    }

    public List<DonHang> getDonHangByWeek(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, day - (current.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY), 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " != " + "'" + context.getString(R.string.status_da_huy) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "getDonHangByWeek: " + selectQuery);

        return excQuery(selectQuery);
    }

    public List<DonHang> getDonHangByMonth(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, 1, 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " != " + "'" + context.getString(R.string.status_da_huy) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "getDonHangByMonth: " + selectQuery);

        return excQuery(selectQuery);
    }

    public List<DonHang> layDonHangHoanThanhTheoNgay(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, day, 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_hoan_thanh) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangHoanThanhTheoNgay: " + selectQuery);

        return excQuery(selectQuery);
    }

    public List<DonHang> layDonHangDangXuLyTheoNgay(long time) {

        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, day, 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_dang_xu_ly) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangDangXuLyTheoNgay: " + selectQuery);

        return excQuery(selectQuery);
    }

    public List<DonHang> layDonHangHoanThanhTheoTuan(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, day - (current.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY), 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_hoan_thanh) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangHoanThanhTheoTuan: " + selectQuery);

        return excQuery(selectQuery);
    }

    public List<DonHang> layDonHangDangXuLyTheoTuan(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, day - (current.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY), 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_dang_xu_ly) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangDangXuLyTheoTuan: " + selectQuery);

        return excQuery(selectQuery);
    }

    public List<DonHang> layDonHangHoanThanhTheoThang(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, 1, 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_hoan_thanh) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangHoanThanhTheoThang: " + selectQuery);

        return excQuery(selectQuery);
    }

    public List<DonHang> layDonHangDangXuLyTheoThang(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, 1, 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_dang_xu_ly) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangDangXuLyTheoThang: " + selectQuery);

        return excQuery(selectQuery);
    }

    private List<DonHang> excQuery(String selectQuery) {
        List<DonHang> donHangs = new ArrayList<>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));
                donHang.setIdKhachHang(cursor.getString(3));
                donHang.setIdSanPhamsFromJsonString(cursor.getString(4));
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
    }

    private boolean checkExits(String id) {
        return getDonHangById(id) != null;
    }
}

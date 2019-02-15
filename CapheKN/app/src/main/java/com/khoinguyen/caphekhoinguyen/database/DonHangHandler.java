package com.khoinguyen.caphekhoinguyen.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DonHangHandler {
    private final static String TAG = "DonHangHandler";

    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private ContentValues values;
    private Context context;

    private KhachHangHandler khachHangHandler;
    private SanPhamHandler sanPhamHandler;

    public DonHangHandler(Context context) {
        this.dbHelper = DBHelper.getInstance(context);
        this.context = context;
        khachHangHandler = new KhachHangHandler(context);
        sanPhamHandler = new SanPhamHandler(context);
    }

    public void insertDonHang(DonHang donHang) {
        db = dbHelper.getWritableDatabase();

        values = new ContentValues();
        values.put(DBConstant.DON_HANG_ID, donHang.getId());
        values.put(DBConstant.DON_HANG_THOI_GIAN_TAO, donHang.getThoiGianTao());
        values.put(DBConstant.DON_HANG_TRANG_THAI, donHang.getTrangThai());
        values.put(DBConstant.DON_HANG_MA_KHACH_HANG, donHang.getKhachHang() != null ? donHang.getKhachHang().getId() : null);
        values.put(DBConstant.DON_HANG_SAN_PHAM, donHang.convertSanPhamsToJsonString());

        db.insert(DBConstant.TABLE_NAME_DON_HANG, null, values);
        values = null;
        db.close();
    }

    public long updateDonHang(DonHang donHang) {
        db = dbHelper.getWritableDatabase();

        values = new ContentValues();
        values.put(DBConstant.DON_HANG_THOI_GIAN_TAO, donHang.getThoiGianTao());
        values.put(DBConstant.DON_HANG_TRANG_THAI, donHang.getTrangThai());
        values.put(DBConstant.DON_HANG_MA_KHACH_HANG, donHang.getKhachHang() != null ? donHang.getKhachHang().getId() : null);
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
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBConstant.TABLE_NAME_DON_HANG, new String[]{DBConstant.DON_HANG_ID,
                        DBConstant.DON_HANG_THOI_GIAN_TAO, DBConstant.DON_HANG_TRANG_THAI, DBConstant.DON_HANG_MA_KHACH_HANG, DBConstant.DON_HANG_SAN_PHAM}, DBConstant.DON_HANG_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DonHang donHang = new DonHang();
        donHang.setId(cursor.getString(0));
        donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
        donHang.setTrangThai(cursor.getString(2));

        String maKH = cursor.getString(3);
        if (!TextUtils.isEmpty(maKH)) {
            KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
            donHang.setKhachHang(khachHang);
        }

        String sanPhams = cursor.getString(4);
        if (!TextUtils.isEmpty(sanPhams)) {
            donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
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
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
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
        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_dang_xu_ly) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
    }

    public List<DonHang> getDonHangDangXuLyByKhachHang(String idKhachHang) {
        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_MA_KHACH_HANG + " = " + String.valueOf(idKhachHang)
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_dang_xu_ly) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
    }

    public List<DonHang> getDonHangHoanThanhByKhachHang(String idKhachHang) {
        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_MA_KHACH_HANG + " = " + String.valueOf(idKhachHang)
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_hoan_thanh) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
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

        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " != " + "'" + context.getString(R.string.status_da_huy) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "getDonHangByDate: " + selectQuery);

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
    }

    public List<DonHang> getDonHangByWeek(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, day - current.get(Calendar.DAY_OF_WEEK_IN_MONTH), 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " != " + "'" + context.getString(R.string.status_da_huy) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "getDonHangByWeek: " + selectQuery);

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
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

        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " != " + "'" + context.getString(R.string.status_da_huy) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "getDonHangByMonth: " + selectQuery);

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
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

        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_hoan_thanh) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangHoanThanhTheoNgay: " + selectQuery);

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
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

        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_dang_xu_ly) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangDangXuLyTheoNgay: " + selectQuery);

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
    }

    public List<DonHang> layDonHangHoanThanhTheoTuan(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, day - current.get(Calendar.DAY_OF_WEEK_IN_MONTH), 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_hoan_thanh) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangHoanThanhTheoTuan: " + selectQuery);

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
    }

    public List<DonHang> layDonHangDangXuLyTheoTuan(long time) {
        Calendar current = Calendar.getInstance();
        current.setTimeInMillis(time);
        int year = current.get(Calendar.YEAR);
        int month = current.get(Calendar.MONTH);
        int day = current.get(Calendar.DAY_OF_MONTH);

        Calendar to = Calendar.getInstance();
        to.set(year, month, day - current.get(Calendar.DAY_OF_WEEK_IN_MONTH), 0, 0, 0);
        to.clear(Calendar.MILLISECOND);

        Calendar from = Calendar.getInstance();
        from.set(year, month, day, 23, 59, 59);
        from.clear(Calendar.MILLISECOND);

        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_dang_xu_ly) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangDangXuLyTheoTuan: " + selectQuery);

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
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

        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_hoan_thanh) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangHoanThanhTheoThang: " + selectQuery);

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
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

        List<DonHang> donHangs = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + DBConstant.TABLE_NAME_DON_HANG
                + " WHERE " + DBConstant.DON_HANG_THOI_GIAN_TAO + " > " + to.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_THOI_GIAN_TAO + " < " + from.getTimeInMillis()
                + " AND " + DBConstant.DON_HANG_TRANG_THAI + " = " + "'" + context.getString(R.string.status_dang_xu_ly) + "'"
                + " ORDER BY " + DBConstant.DON_HANG_THOI_GIAN_TAO + " DESC";

        LogUtils.d(TAG, "layDonHangDangXuLyTheoThang: " + selectQuery);

        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DonHang donHang = new DonHang();
                donHang.setId(cursor.getString(0));
                donHang.setThoiGianTao(Long.valueOf(cursor.getString(1)));
                donHang.setTrangThai(cursor.getString(2));

                String maKH = cursor.getString(3);
                if (!TextUtils.isEmpty(maKH)) {
                    KhachHang khachHang = khachHangHandler.getKhachHangById(maKH);
                    donHang.setKhachHang(khachHang);
                }

                String sanPhams = cursor.getString(4);
                if (!TextUtils.isEmpty(sanPhams)) {
                    donHang.setSanPhamsFromJsonString(sanPhams, sanPhamHandler);
                }
                donHangs.add(donHang);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return donHangs;
    }
}

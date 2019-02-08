package com.khoinguyen.caphekhoinguyen.database;

public class DBConstant {

    public static final String TABLE_NAME_DON_HANG = "donhang";
    public static final String DON_HANG_ID = "id";
    public static final String DON_HANG_THOI_GIAN_TAO = "thoi_gian_tao";
    public static final String DON_HANG_TRANG_THAI = "trang_thai";
    public static final String DON_HANG_MA_KHACH_HANG = "ma_khach_hang";
    public static final String DON_HANG_MA_SAN_PHAM = "ma_san_pham";

    public static final String TABLE_NAME_SAN_PHAM = "sanpham";
    public static final String SAN_PHAM_ID = "id";
    public static final String SAN_PHAM_TEN = "ten";
    public static final String SAN_PHAM_DON_GIA = "don_gia";

    public static final String TABLE_NAME_KHACH_HANG = "khachhang";
    public static final String KHACH_HANG_ID = "id";
    public static final String KHACH_HANG_TEN = "ten";
    public static final String KHACH_HANG_SDT = "sdt";

    public static final String SQL_CREATE_DON_HANG = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_DON_HANG + " (" +
            DON_HANG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DON_HANG_THOI_GIAN_TAO + " INTEGER, " +
            DON_HANG_TRANG_THAI + " TEXT, " +
            DON_HANG_MA_KHACH_HANG + " TEXT," +
            DON_HANG_MA_SAN_PHAM + " TEXT)";

    public static final String SQL_DROP_DON_HANG = "DROP TABLE IF EXISTS " + TABLE_NAME_DON_HANG;

    public static final String SQL_CREATE_SAN_PHAM = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_SAN_PHAM + " (" +
            SAN_PHAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SAN_PHAM_TEN + " TEXT, " +
            SAN_PHAM_DON_GIA + " TEXT)";

    public static final String SQL_DROP_SAN_PHAM = "DROP TABLE IF EXISTS " + TABLE_NAME_SAN_PHAM;

    public static final String SQL_CREATE_KHACH_HANG = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_KHACH_HANG + " (" +
            KHACH_HANG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KHACH_HANG_TEN + " TEXT, " +
            KHACH_HANG_SDT + " TEXT)";

    public static final String SQL_DROP_KHACH_HANG = "DROP TABLE IF EXISTS " + TABLE_NAME_KHACH_HANG;
}

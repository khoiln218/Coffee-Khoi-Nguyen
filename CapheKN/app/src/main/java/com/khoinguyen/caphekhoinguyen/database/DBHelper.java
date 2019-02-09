package com.khoinguyen.caphekhoinguyen.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.khoinguyen.caphekhoinguyen.utils.LogUtils;

public class DBHelper extends SQLiteOpenHelper {
    private final String TAG = "DBHelper";

    public static final String DATABASE_NAME = "khoinguyen.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        LogUtils.d(TAG, "init");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstant.SQL_CREATE_DON_HANG);
        db.execSQL(DBConstant.SQL_CREATE_KHACH_HANG);
        db.execSQL(DBConstant.SQL_CREATE_SAN_PHAM);
        LogUtils.d(TAG, "onCreate successfylly");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DBConstant.SQL_DROP_DON_HANG);
        db.execSQL(DBConstant.SQL_DROP_KHACH_HANG);
        db.execSQL(DBConstant.SQL_DROP_SAN_PHAM);
        onCreate(db);
        LogUtils.d(TAG, "onUpgrade successfylly");
    }
}

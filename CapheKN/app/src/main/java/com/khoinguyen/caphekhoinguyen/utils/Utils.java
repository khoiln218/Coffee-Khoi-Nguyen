package com.khoinguyen.caphekhoinguyen.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.khoinguyen.caphekhoinguyen.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class Utils {
    private static ProgressDialog mProgressDialog = null;
    private static Toast mToast;

    public static void showProgressDialog(Context context) {
        if (mProgressDialog == null)
            mProgressDialog = createProgressDialog(context);
        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (null != mProgressDialog) {
            try {
                mProgressDialog.dismiss();
            } catch (Exception e) {
                LogUtils.writeException(e);
            }
        }
        mProgressDialog = null;
    }

    private static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressTheme);
        progressDialog.setMessage("Đang tải dữ liệu");
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        return progressDialog;
    }

    public static void showToast(Context context, String message) {
        if (context == null) {
            return;
        }
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        mToast.show();
    }

    public static void hideKeyBoard(Activity context) {
        // Check if no view has focus:
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Activity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static String convTimestamp(long timestamp, String pattern) {

        if (TextUtils.isEmpty(pattern)) {
            pattern = "dd/MM/yyyy HH:mm:ss";
        }
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        /* date formatter in local timezone */
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(tz);

        /* print your timestamp and double check it's the date you expect */
        return sdf.format(new Date(timestamp));
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}

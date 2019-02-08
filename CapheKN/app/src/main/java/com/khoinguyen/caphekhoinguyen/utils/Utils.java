package com.khoinguyen.caphekhoinguyen.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.khoinguyen.caphekhoinguyen.R;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Utils {
    private static ProgressDialog mProgressDialog = null;
    private static Toast mToast;
    private static final String TIME24HOURS_PATTERN = "((0|1)?[0-9]|2[0-3]):[0-5][0-9]";

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

    public static ProgressDialog createProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context, R.style.ProgressTheme);
        progressDialog.setMessage("");
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

    public static boolean isEmpty(TextView editText, String message) {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            editText.setError(message);
            return true;
        }
        return false;
    }

    /**
     * Validate time in 24 hours format with regular expression
     *
     * @return true valid time fromat, false invalid time format
     */
    public static boolean isTimeValid(TextView editText, String error) {
        Pattern pattern = Pattern.compile(TIME24HOURS_PATTERN);
        Matcher matcher = pattern.matcher(editText.getText());
        if (!matcher.matches()) {
            editText.setError(error);
            return false;
        }
        return true;
    }

    public static void hideKeyBoard(Context context, View view) {
        // Check if no view has focus:
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showSoftKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * Convert timestamp to local time in "pattern" string
     *
     * @param timestamp timestamp in milliseconds
     * @param pattern   the pattern of output format, example "M/d/yyyy h:m:s aa", "dd/MM/yyyy HH:mm:ss"
     * @return
     */
    public static String convTimestamp(long timestamp, String pattern) {

        if (TextUtils.isEmpty(pattern)) {
            pattern = "dd/MM/yyyy hh:mm:ss";
        }
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        /* date formatter in local timezone */
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(tz);

        /* print your timestamp and double check it's the date you expect */
        String localTime = sdf.format(new Date(timestamp));
        return localTime;
    }

    /**
     * Convert timestamp to local time with default pattern "dd/MM/yyyy hh:mm:ss"
     *
     * @param timestamp timestamp in milliseconds
     * @return
     */
    public static String convTimestamp(long timestamp) {
        return convTimestamp(timestamp, "");
    }

    public static long getTimeStamp(String inputString, String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            pattern = "dd/MM/yyyy hh:mm:ss";
        }
        DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        Date resultDate = null;
        try {
            resultDate = dateFormat.parse(inputString.trim());
        } catch (ParseException e) {
            LogUtils.writeException(e);
        }
        return resultDate != null ? resultDate.getTime() : System.currentTimeMillis();
    }

    public static long getTimeStamp(String inputString) {
        return getTimeStamp(inputString, "");
    }
}

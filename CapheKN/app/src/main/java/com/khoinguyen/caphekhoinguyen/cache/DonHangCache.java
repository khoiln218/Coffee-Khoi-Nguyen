package com.khoinguyen.caphekhoinguyen.cache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.model.DonHang;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class DonHangCache {
    @SuppressLint("StaticFieldLeak")
    private static DonHangCache INSTANCE = null;

    private static final List<DonHang> donHangs = new ArrayList<>();
    private Context mContext;

    private DonHangCache(Context context) {
        mContext = context;
    }

    public synchronized static DonHangCache getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DonHangCache(context);
        }
        return (INSTANCE);
    }

    public List<DonHang> layDanhSachDonHang() {
        return donHangs;
    }

    public DonHang layDonHangTheoId(String idDonHang) {
        for (DonHang donHang : donHangs)
            if (TextUtils.equals(donHang.getId(), idDonHang))
                return donHang;
        return null;
    }

    public List<DonHang> layDonHangDangXuLy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly)))
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
    }

    public List<DonHang> layDonHangDangXuLyTheoKhachHang(String idKhachHang) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))
                            && TextUtils.equals(entry.getIdKhachHang(), idKhachHang))
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))
                        && TextUtils.equals(entry.getIdKhachHang(), idKhachHang)) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
    }

    public List<DonHang> layDonHangHoanThanhTheoKhachHang(String idKhachHang) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_hoan_thanh))
                            && TextUtils.equals(entry.getIdKhachHang(), idKhachHang))
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_hoan_thanh))
                        && TextUtils.equals(entry.getIdKhachHang(), idKhachHang)) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
    }

    public List<DonHang> layDonHangTheoNgay(long time) {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> !TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_da_huy))
                            && entry.getThoiGianTao() > to.getTimeInMillis()
                            && entry.getThoiGianTao() < from.getTimeInMillis())
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (!TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_da_huy))
                        && entry.getThoiGianTao() > to.getTimeInMillis()
                        && entry.getThoiGianTao() < from.getTimeInMillis()) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
    }

    public List<DonHang> layDonHangTheoTuan(long time) {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> !TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_da_huy))
                            && entry.getThoiGianTao() > to.getTimeInMillis()
                            && entry.getThoiGianTao() < from.getTimeInMillis())
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (!TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_da_huy))
                        && entry.getThoiGianTao() > to.getTimeInMillis()
                        && entry.getThoiGianTao() < from.getTimeInMillis()) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
    }

    public List<DonHang> layDonHangTheoThang(long time) {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> !TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_da_huy))
                            && entry.getThoiGianTao() > to.getTimeInMillis()
                            && entry.getThoiGianTao() < from.getTimeInMillis())
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (!TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_da_huy))
                        && entry.getThoiGianTao() > to.getTimeInMillis()
                        && entry.getThoiGianTao() < from.getTimeInMillis()) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_hoan_thanh))
                            && entry.getThoiGianTao() > to.getTimeInMillis()
                            && entry.getThoiGianTao() < from.getTimeInMillis())
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_hoan_thanh))
                        && entry.getThoiGianTao() > to.getTimeInMillis()
                        && entry.getThoiGianTao() < from.getTimeInMillis()) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))
                            && entry.getThoiGianTao() > to.getTimeInMillis()
                            && entry.getThoiGianTao() < from.getTimeInMillis())
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))
                        && entry.getThoiGianTao() > to.getTimeInMillis()
                        && entry.getThoiGianTao() < from.getTimeInMillis()) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_hoan_thanh))
                            && entry.getThoiGianTao() > to.getTimeInMillis()
                            && entry.getThoiGianTao() < from.getTimeInMillis())
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_hoan_thanh))
                        && entry.getThoiGianTao() > to.getTimeInMillis()
                        && entry.getThoiGianTao() < from.getTimeInMillis()) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))
                            && entry.getThoiGianTao() > to.getTimeInMillis()
                            && entry.getThoiGianTao() < from.getTimeInMillis())
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))
                        && entry.getThoiGianTao() > to.getTimeInMillis()
                        && entry.getThoiGianTao() < from.getTimeInMillis()) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_hoan_thanh))
                            && entry.getThoiGianTao() > to.getTimeInMillis()
                            && entry.getThoiGianTao() < from.getTimeInMillis())
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_hoan_thanh))
                        && entry.getThoiGianTao() > to.getTimeInMillis()
                        && entry.getThoiGianTao() < from.getTimeInMillis()) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return donHangs.stream()
                    .filter(entry -> TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))
                            && entry.getThoiGianTao() > to.getTimeInMillis()
                            && entry.getThoiGianTao() < from.getTimeInMillis())
                    .collect(Collectors.toList());
        } else {
            List<DonHang> donHangList = new ArrayList<>();
            for (DonHang entry : donHangs) {
                if (TextUtils.equals(entry.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))
                        && entry.getThoiGianTao() > to.getTimeInMillis()
                        && entry.getThoiGianTao() < from.getTimeInMillis()) {
                    donHangList.add(entry);
                }
            }
            return donHangList;
        }
    }

    public void capNhatHoacThemDonHang(DonHang donHang) {
        if (layDonHangTheoId(donHang.getId()) != null)
            capNhatDonHang(donHang);
        else {
            for (int i = 0; i < donHangs.size(); i++)
                if (donHang.getThoiGianTao() > donHangs.get(i).getThoiGianTao()) {
                    donHangs.add(i, donHang);
                    return;
                }
            donHangs.add(donHang);
        }
    }

    public void capNhatDonHang(DonHang donHang) {
        for (int i = 0; i < donHangs.size(); i++)
            if (TextUtils.equals(donHangs.get(i).getId(), donHang.getId())) {
                donHangs.set(i, donHang);
                return;
            }
    }
}

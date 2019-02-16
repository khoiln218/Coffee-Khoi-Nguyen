package com.khoinguyen.caphekhoinguyen.realtimedatabase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;

import java.util.List;

public class RealtimeDatabaseController {
    private static final String TAG = "RealtimeDatabaseController";

    private static RealtimeDatabaseController INSTANCE = null;

    private DatabaseReference mDatabase;
    private DatabaseReference mDonHangDatabase;
    private DatabaseReference mKhachHangDatabase;
    private DatabaseReference mSanPhamDatabase;

    private ChildEventListener mDonHangChildEventListener;
    private ChildEventListener mKhachHangChildEventListener;
    private ChildEventListener mSanPhamChildEventListener;

    private RealtimeDatabaseController() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDonHangDatabase = mDatabase.child(getUid()).child("donhang");
        mKhachHangDatabase = mDatabase.child(getUid()).child("khachhang");
        mSanPhamDatabase = mDatabase.child(getUid()).child("sanpham");
    }

    public static RealtimeDatabaseController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RealtimeDatabaseController();
        }
        return (INSTANCE);
    }

    public String genKeyDonHang() {
        return mDonHangDatabase.push().getKey();
    }

    public String genKeyKhachHang() {
        return mKhachHangDatabase.push().getKey();
    }

    public String genKeySanPham() {
        return mSanPhamDatabase.push().getKey();
    }

    public void dongBo(final Context context) {
        LogUtils.d(TAG, "dongBo");
        ValueEventListener donHangValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "DonHang - onDataChange");
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    DBController.getInstance(context).themDonHangDenDB(ds.getValue(DonHang.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        ValueEventListener khachHangValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "KhachHang - onDataChange");
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    DBController.getInstance(context).themKhachHangDenDB(ds.getValue(KhachHang.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        ValueEventListener sanPhamValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "SanPham - onDataChange");
                for (DataSnapshot ds : dataSnapshot.getChildren())
                    DBController.getInstance(context).themSanPhamDenDB(ds.getValue(SanPham.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDonHangDatabase.addListenerForSingleValueEvent(donHangValueEventListener);
        mKhachHangDatabase.addListenerForSingleValueEvent(khachHangValueEventListener);
        mSanPhamDatabase.addListenerForSingleValueEvent(sanPhamValueEventListener);
    }

    public void startListerner(final Context context) {
        LogUtils.d(TAG, "startListerner");
        mDonHangChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "DonHang - onChildAdded: " + dataSnapshot.getKey());
                DBController.getInstance(context).themDonHangDenDB(dataSnapshot.getValue(DonHang.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "DonHang - onChildChanged: " + dataSnapshot.getKey());
                DBController.getInstance(context).capNhatDonHangDenDB(dataSnapshot.getValue(DonHang.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "DonHang - onChildRemoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "DonHang - onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                LogUtils.d(TAG, "DonHang - onCancelled: " + databaseError.getMessage());
            }
        };

        mKhachHangChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "KhachHang - onChildAdded: " + dataSnapshot.getKey());
                DBController.getInstance(context).themKhachHangDenDB(dataSnapshot.getValue(KhachHang.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "KhachHang - onChildChanged: " + dataSnapshot.getKey());
                DBController.getInstance(context).capNhatKhachHangDenDB(dataSnapshot.getValue(KhachHang.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "KhachHang - onChildRemoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "KhachHang - onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                LogUtils.d(TAG, "KhachHang - onCancelled: " + databaseError.getMessage());
            }
        };

        mSanPhamChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "SanPham - onChildAdded: " + dataSnapshot.getKey());
                DBController.getInstance(context).themSanPhamDenDB(dataSnapshot.getValue(SanPham.class));
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "SanPham - onChildChanged: " + dataSnapshot.getKey());
                DBController.getInstance(context).capNhatSanPhamDenDB(dataSnapshot.getValue(SanPham.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "SanPham - onChildRemoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "SanPham - onChildMoved: " + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                LogUtils.d(TAG, "SanPham - onCancelled: " + databaseError.getMessage());
            }
        };

        mDonHangDatabase.addChildEventListener(mDonHangChildEventListener);
        mKhachHangDatabase.addChildEventListener(mKhachHangChildEventListener);
        mSanPhamDatabase.addChildEventListener(mSanPhamChildEventListener);
    }

    private void stopChildListerner() {
        LogUtils.d(TAG, "stopChildListerner");
        if (mDonHangChildEventListener != null)
            mDonHangDatabase.removeEventListener(mDonHangChildEventListener);
        if (mKhachHangChildEventListener != null)
            mKhachHangDatabase.removeEventListener(mKhachHangChildEventListener);
        if (mSanPhamChildEventListener != null)
            mSanPhamDatabase.removeEventListener(mSanPhamChildEventListener);
    }

    public void stopListerner() {
        stopChildListerner();
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void taiSanPhamLenServer(List<SanPham> sanPhams) {
        LogUtils.d(TAG, "taiSanPhamLenServer");
        for (SanPham sanPham : sanPhams)
            themSanPham(sanPham);
    }

    public void taiKhachHangLenServer(List<KhachHang> khachHangs) {
        LogUtils.d(TAG, "taiKhachHangLenServer");
        for (KhachHang khachHang : khachHangs)
            themKhachHang(khachHang);
    }

    public void taiDonHangLenServer(List<DonHang> donHangs) {
        LogUtils.d(TAG, "taiDonHangLenServer");
        for (DonHang donHang : donHangs)
            themDonHang(donHang);
    }

    public void themDonHang(DonHang donHang) {
        mDonHangDatabase.child(donHang.getId()).setValue(donHang);
    }

    public void capNhatDonHang(DonHang donHang) {
        mDonHangDatabase.child(donHang.getId()).setValue(donHang);
    }

    public void themKhachHang(KhachHang khachHang) {
        mKhachHangDatabase.child(khachHang.getId()).setValue(khachHang);
    }

    public void capNhatKhachHang(KhachHang khachHang) {
        mKhachHangDatabase.child(khachHang.getId()).setValue(khachHang);
    }

    public void themSanPham(SanPham sanPham) {
        mSanPhamDatabase.child(sanPham.getId()).setValue(sanPham);
    }

    public void capNhatSanPham(SanPham sanPham) {
        mSanPhamDatabase.child(sanPham.getId()).setValue(sanPham);
    }
}

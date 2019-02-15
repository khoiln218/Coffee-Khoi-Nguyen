package com.khoinguyen.caphekhoinguyen.realtimedatabase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;

import java.util.List;

public class RealtimeDatabaseController {
    private static RealtimeDatabaseController INSTANCE = null;

    private DatabaseReference mDatabase;
    private DatabaseReference mDonHangDatabase;
    private DatabaseReference mKhachHangDatabase;
    private DatabaseReference mSanPhamDatabase;

    private ValueEventListener mDonHangValueEventListener;
    private ValueEventListener mKhachHangValueEventListener;
    private ValueEventListener mSanPhamValueEventListener;

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

    public void dongBo() {
        stopVaLueListerner();

        mDonHangValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mKhachHangValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mSanPhamValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDonHangDatabase.addListenerForSingleValueEvent(mDonHangValueEventListener);
        mKhachHangDatabase.addListenerForSingleValueEvent(mKhachHangValueEventListener);
        mSanPhamDatabase.addListenerForSingleValueEvent(mSanPhamValueEventListener);
    }

    public void startListerner() {
        stopChildListerner();
        mDonHangChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mKhachHangChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mSanPhamChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDonHangDatabase.addChildEventListener(mDonHangChildEventListener);
        mKhachHangDatabase.addChildEventListener(mKhachHangChildEventListener);
        mSanPhamDatabase.addChildEventListener(mSanPhamChildEventListener);
    }

    private void stopVaLueListerner() {
        if (mDonHangValueEventListener != null)
            mDonHangDatabase.removeEventListener(mDonHangValueEventListener);
        if (mKhachHangValueEventListener != null)
            mKhachHangDatabase.removeEventListener(mKhachHangValueEventListener);
        if (mSanPhamValueEventListener != null)
            mSanPhamDatabase.removeEventListener(mSanPhamValueEventListener);
    }

    private void stopChildListerner() {
        if (mDonHangChildEventListener != null)
            mDonHangDatabase.removeEventListener(mDonHangChildEventListener);
        if (mKhachHangChildEventListener != null)
            mKhachHangDatabase.removeEventListener(mKhachHangChildEventListener);
        if (mSanPhamChildEventListener != null)
            mSanPhamDatabase.removeEventListener(mSanPhamChildEventListener);
    }

    public void stopListerner() {
        stopVaLueListerner();
        stopChildListerner();
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void taiSanPhamLenServer(List<SanPham> sanPhams) {
    }

    public void taiKhachHangLenServer(List<KhachHang> khachHangs) {
    }

    public void taiDonHangLenServer(List<DonHang> donHangs) {
    }

    public void themDonHang(DonHang donHang) {
    }

    public void capNhatDonHang(DonHang donHang) {
    }

    public void themKhachHang(KhachHang khachHang) {
    }

    public void capNhatKhachHang(KhachHang khachHang) {
    }

    public void themSanPham(SanPham sanPham) {
    }

    public void capNhatSanPham(SanPham sanPham) {
    }
}

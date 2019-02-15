package com.khoinguyen.caphekhoinguyen.realtimedatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RealtimeDatabaseController {
    private DatabaseReference mDatabase;
    private DatabaseReference mDonHangDatabase;
    private DatabaseReference mKhachHangDatabase;
    private DatabaseReference mSanPhamDatabase;

    private ValueEventListener mDonHangValueEventListener;
    private ValueEventListener mKhachHangValueEventListener;
    private ValueEventListener mSanPhamValueEventListener;

    public RealtimeDatabaseController() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDonHangDatabase = mDatabase.child(getUid()).child("donhang");
        mKhachHangDatabase = mDatabase.child(getUid()).child("khachhang");
        mSanPhamDatabase = mDatabase.child(getUid()).child("sanpham");
    }

    public void startListerner() {
        if (mDonHangValueEventListener != null)
            mDonHangDatabase.removeEventListener(mDonHangValueEventListener);
        if (mKhachHangValueEventListener != null)
            mKhachHangDatabase.removeEventListener(mKhachHangValueEventListener);
        if (mSanPhamValueEventListener != null)
            mSanPhamDatabase.removeEventListener(mSanPhamValueEventListener);

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

    public void stopListerner() {
        if (mDonHangValueEventListener != null)
            mDonHangDatabase.removeEventListener(mDonHangValueEventListener);
        if (mKhachHangValueEventListener != null)
            mKhachHangDatabase.removeEventListener(mKhachHangValueEventListener);
        if (mSanPhamValueEventListener != null)
            mSanPhamDatabase.removeEventListener(mSanPhamValueEventListener);
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

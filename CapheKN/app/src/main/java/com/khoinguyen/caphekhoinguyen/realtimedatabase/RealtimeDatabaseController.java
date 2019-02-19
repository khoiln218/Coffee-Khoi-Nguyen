package com.khoinguyen.caphekhoinguyen.realtimedatabase;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.event.DonHangEvent;
import com.khoinguyen.caphekhoinguyen.event.KhachHangEvent;
import com.khoinguyen.caphekhoinguyen.event.NetStatusEvent;
import com.khoinguyen.caphekhoinguyen.event.SanPhamEvent;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RealtimeDatabaseController {
    private static final String TAG = "RealtimeDatabaseController";

    private static RealtimeDatabaseController INSTANCE = null;

    private DatabaseReference mDatabase;
    private DatabaseReference mDonHangDatabase;
    private DatabaseReference mKhachHangDatabase;
    private DatabaseReference mSanPhamDatabase;
    private DatabaseReference mConnectedRef;

    private ChildEventListener mDonHangChildEventListener;
    private ChildEventListener mKhachHangChildEventListener;
    private ChildEventListener mSanPhamChildEventListener;

    private ValueEventListener mConnectValueEventListener;

    private boolean isLoadDonHangComplete = false;
    private boolean isLoadKhachHangComplete = false;
    private boolean isLoadSanPhamComplete = false;

    private RealtimeDatabaseController() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDonHangDatabase = mDatabase.child(getUid()).child("donhang");
        mKhachHangDatabase = mDatabase.child(getUid()).child("khachhang");
        mSanPhamDatabase = mDatabase.child(getUid()).child("sanpham");
        mConnectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
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

        startLoad(context);

        ValueEventListener donHangValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "DonHang - onDataChange");
                new AsyncTask<Void, Integer, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                            DBController.getInstance(context).themDonHangDenDB(ds.getValue(DonHang.class));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        isLoadDonHangComplete = true;
                        stopLoad();
                    }
                }.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        ValueEventListener khachHangValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "KhachHang - onDataChange");
                new AsyncTask<Void, Integer, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                            DBController.getInstance(context).themKhachHangDenDB(ds.getValue(KhachHang.class));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        isLoadKhachHangComplete = true;
                        stopLoad();
                    }
                }.execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        ValueEventListener sanPhamValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "SanPham - onDataChange");
                new AsyncTask<Void, Integer, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                            DBController.getInstance(context).themSanPhamDenDB(ds.getValue(SanPham.class));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);
                        isLoadSanPhamComplete = true;
                        stopLoad();
                    }
                }.execute();
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
                DonHang donHang = dataSnapshot.getValue(DonHang.class);
                DBController.getInstance(context).themDonHangDenDB(donHang);
                DonHangEvent event = new DonHangEvent();
                event.setDonHang(donHang);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "DonHang - onChildChanged: " + dataSnapshot.getKey());
                DonHang donHang = dataSnapshot.getValue(DonHang.class);
                DBController.getInstance(context).capNhatDonHangDenDB(donHang);
                DonHangEvent event = new DonHangEvent();
                event.setDonHang(donHang);
                EventBus.getDefault().post(event);
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
                KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);
                DBController.getInstance(context).themKhachHangDenDB(khachHang);
                KhachHangEvent event = new KhachHangEvent();
                event.setKhachHang(khachHang);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "KhachHang - onChildChanged: " + dataSnapshot.getKey());
                KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);
                DBController.getInstance(context).capNhatKhachHangDenDB(khachHang);
                KhachHangEvent event = new KhachHangEvent();
                event.setKhachHang(khachHang);
                EventBus.getDefault().post(event);
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
                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                DBController.getInstance(context).themSanPhamDenDB(sanPham);
                SanPhamEvent event = new SanPhamEvent();
                event.setSanPham(sanPham);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "SanPham - onChildChanged: " + dataSnapshot.getKey());
                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                DBController.getInstance(context).capNhatSanPhamDenDB(sanPham);
                SanPhamEvent event = new SanPhamEvent();
                event.setSanPham(sanPham);
                EventBus.getDefault().post(event);
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

        mConnectValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                NetStatusEvent event = new NetStatusEvent();
                event.setConnect(connected);
                EventBus.getDefault().post(event);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                LogUtils.w(TAG, "Listener was cancelled");
            }
        };

        mDonHangDatabase.addChildEventListener(mDonHangChildEventListener);
        mKhachHangDatabase.addChildEventListener(mKhachHangChildEventListener);
        mSanPhamDatabase.addChildEventListener(mSanPhamChildEventListener);
        mConnectedRef.addValueEventListener(mConnectValueEventListener);
    }

    private void startLoad(Context context) {
        LogUtils.d(TAG, "startLoad");
        isLoadDonHangComplete = false;
        isLoadKhachHangComplete = false;
        isLoadSanPhamComplete = false;
        Utils.showProgressDialog(context);
    }

    private void stopLoad() {
        if (isLoadDonHangComplete && isLoadKhachHangComplete && isLoadSanPhamComplete) {
            LogUtils.d(TAG, "stopLoad");
            Utils.hideProgressDialog();
        }
    }

    public void stopListerner() {
        LogUtils.d(TAG, "stopListerner");
        if (mDonHangChildEventListener != null)
            mDonHangDatabase.removeEventListener(mDonHangChildEventListener);
        if (mKhachHangChildEventListener != null)
            mKhachHangDatabase.removeEventListener(mKhachHangChildEventListener);
        if (mSanPhamChildEventListener != null)
            mSanPhamDatabase.removeEventListener(mSanPhamChildEventListener);
        if (mConnectValueEventListener != null)
            mConnectedRef.removeEventListener(mConnectValueEventListener);
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

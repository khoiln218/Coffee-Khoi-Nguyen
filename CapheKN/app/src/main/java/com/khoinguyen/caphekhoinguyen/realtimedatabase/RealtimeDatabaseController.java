package com.khoinguyen.caphekhoinguyen.realtimedatabase;

import android.content.Context;
import android.os.Handler;
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
    private static final int TIME_INTERVAL = 3500;

    private static RealtimeDatabaseController INSTANCE = null;

    private Context mContext;

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

    private Handler handler = new Handler();
    private Runnable rDonHang = new Runnable() {
        @Override
        public void run() {
            isLoadDonHangComplete = true;
            stopLoad();
        }
    };

    private Runnable rKhachHang = new Runnable() {
        @Override
        public void run() {
            isLoadKhachHangComplete = true;
            stopLoad();
        }
    };

    private Runnable rSanPham = new Runnable() {
        @Override
        public void run() {
            isLoadSanPhamComplete = true;
            stopLoad();
        }
    };

    private RealtimeDatabaseController(Context context) {
        mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uid = getUid();
        mDonHangDatabase = mDatabase.child(uid).child("donhang");
        mKhachHangDatabase = mDatabase.child(uid).child("khachhang");
        mSanPhamDatabase = mDatabase.child(uid).child("sanpham");
        mConnectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
    }

    public static RealtimeDatabaseController getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RealtimeDatabaseController(context);
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
        LogUtils.d(TAG, "dongBo");

        startLoad();

        ValueEventListener donHangValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "DonHang - onDataChange");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                            DBController.getInstance(mContext).themDonHangDenDB(ds.getValue(DonHang.class));
                        isLoadDonHangComplete = true;
                        stopLoad();
                    }
                }).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        ValueEventListener khachHangValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "KhachHang - onDataChange");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                            DBController.getInstance(mContext).themKhachHangDenDB(ds.getValue(KhachHang.class));
                        isLoadKhachHangComplete = true;
                        stopLoad();
                    }
                }).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        ValueEventListener sanPhamValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "SanPham - onDataChange");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                            DBController.getInstance(mContext).themSanPhamDenDB(ds.getValue(SanPham.class));
                        isLoadSanPhamComplete = true;
                        stopLoad();
                    }
                }).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        mDonHangDatabase.addListenerForSingleValueEvent(donHangValueEventListener);
        mKhachHangDatabase.addListenerForSingleValueEvent(khachHangValueEventListener);
        mSanPhamDatabase.addListenerForSingleValueEvent(sanPhamValueEventListener);
    }

    public void startListerner() {
        LogUtils.d(TAG, "startListerner");

        startLoad();

        mDonHangChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "DonHang - onChildAdded: " + dataSnapshot.getKey());
                final DonHang donHang = dataSnapshot.getValue(DonHang.class);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBController.getInstance(mContext).themDonHangDenDB(donHang);
                        if (isLoadDonHangComplete) {
                            DonHangEvent event = new DonHangEvent();
                            event.setDonHang(donHang);
                            EventBus.getDefault().post(event);
                        } else {
                            handler.removeCallbacks(rDonHang);
                            handler.postDelayed(rDonHang, TIME_INTERVAL);
                        }
                    }
                }).start();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "DonHang - onChildChanged: " + dataSnapshot.getKey());
                DonHang donHang = dataSnapshot.getValue(DonHang.class);
                DBController.getInstance(mContext).capNhatDonHangDenDB(donHang);
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
                final KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBController.getInstance(mContext).themKhachHangDenDB(khachHang);
                        if (isLoadKhachHangComplete) {
                            KhachHangEvent event = new KhachHangEvent();
                            event.setKhachHang(khachHang);
                            EventBus.getDefault().post(event);
                        } else {
                            handler.removeCallbacks(rKhachHang);
                            handler.postDelayed(rKhachHang, TIME_INTERVAL);
                        }
                    }
                }).start();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "KhachHang - onChildChanged: " + dataSnapshot.getKey());
                KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);
                DBController.getInstance(mContext).capNhatKhachHangDenDB(khachHang);
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
                final SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DBController.getInstance(mContext).themSanPhamDenDB(sanPham);
                        if (isLoadSanPhamComplete) {
                            SanPhamEvent event = new SanPhamEvent();
                            event.setSanPham(sanPham);
                            EventBus.getDefault().post(event);
                        } else {
                            handler.removeCallbacks(rSanPham);
                            handler.postDelayed(rSanPham, TIME_INTERVAL);
                        }
                    }
                }).start();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "SanPham - onChildChanged: " + dataSnapshot.getKey());
                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                DBController.getInstance(mContext).capNhatSanPhamDenDB(sanPham);
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

    private void startLoad() {
        LogUtils.d(TAG, "startLoad");
        isLoadDonHangComplete = false;
        isLoadKhachHangComplete = false;
        isLoadSanPhamComplete = false;
        Utils.showProgressDialog(mContext);
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

package com.khoinguyen.caphekhoinguyen.realtimedatabase;

import android.annotation.SuppressLint;
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
import com.khoinguyen.caphekhoinguyen.event.LoadCompleteEvent;
import com.khoinguyen.caphekhoinguyen.event.NetStatusEvent;
import com.khoinguyen.caphekhoinguyen.event.SanPhamEvent;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.model.User;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Objects;

public class RealtimeDatabaseController {
    private static final String TAG = "RealtimeDatabaseController";
    private static final int TIME_INTERVAL = 1000;

    @SuppressLint("StaticFieldLeak")
    private static RealtimeDatabaseController INSTANCE = null;

    private Context mContext;

    private DatabaseReference mDonHangDatabase;
    private DatabaseReference mKhachHangDatabase;
    private DatabaseReference mSanPhamDatabase;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mConnectedRef;

    private ChildEventListener mDonHangChildEventListener;
    private ChildEventListener mKhachHangChildEventListener;
    private ChildEventListener mSanPhamChildEventListener;

    private ValueEventListener mConnectValueEventListener;

    private boolean isLoadDonHangComplete = false;
    private boolean isLoadKhachHangComplete = false;
    private boolean isLoadSanPhamComplete = false;

    private Handler handler = new Handler();
    private Runnable rDonHang = () -> {
        isLoadDonHangComplete = true;
        stopLoad();
    };

    private Runnable rKhachHang = () -> {
        isLoadKhachHangComplete = true;
        stopLoad();
    };

    private Runnable rSanPham = () -> {
        isLoadSanPhamComplete = true;
        stopLoad();
    };

    private RealtimeDatabaseController(Context context) {
        mContext = context;
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        String uid = getUid();
        mDonHangDatabase = database.child(uid).child("donhang");
        mKhachHangDatabase = database.child(uid).child("khachhang");
        mSanPhamDatabase = database.child(uid).child("sanpham");
        mUserDatabase = database.child("users");
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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "DonHang - onDataChange");
                new Thread(() -> {
                    handler.removeCallbacks(rDonHang);
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                        DBController.getInstance(mContext).capNhatHoacThemDonHangDenCache(ds.getValue(DonHang.class));
                    isLoadDonHangComplete = true;
                    stopLoad();
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        ValueEventListener khachHangValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "KhachHang - onDataChange");
                new Thread(() -> {
                    handler.removeCallbacks(rKhachHang);
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                        DBController.getInstance(mContext).capNhatHoacThemKhachHangDenCache(ds.getValue(KhachHang.class));
                    isLoadKhachHangComplete = true;
                    stopLoad();
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };

        ValueEventListener sanPhamValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LogUtils.d(TAG, "SanPham - onDataChange");
                new Thread(() -> {
                    handler.removeCallbacks(rSanPham);
                    for (DataSnapshot ds : dataSnapshot.getChildren())
                        DBController.getInstance(mContext).capNhatHoacThemSanPhamDenCache(ds.getValue(SanPham.class));
                    isLoadSanPhamComplete = true;
                    stopLoad();
                }).start();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
                DonHang donHang = dataSnapshot.getValue(DonHang.class);
                DBController.getInstance(mContext).capNhatHoacThemDonHangDenCache(donHang);
                if (isLoadDonHangComplete) {
                    DonHangEvent event = new DonHangEvent();
                    event.setDonHang(donHang);
                    EventBus.getDefault().post(event);
                } else {
                    handler.removeCallbacks(rDonHang);
                    handler.postDelayed(rDonHang, TIME_INTERVAL);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "DonHang - onChildChanged: " + dataSnapshot.getKey());
                DonHang donHang = dataSnapshot.getValue(DonHang.class);
                DBController.getInstance(mContext).capNhatDonHangDenCache(donHang);
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
                DBController.getInstance(mContext).capNhatHoacThemKhachHangDenCache(khachHang);
                if (isLoadKhachHangComplete) {
                    KhachHangEvent event = new KhachHangEvent();
                    event.setKhachHang(khachHang);
                    EventBus.getDefault().post(event);
                } else {
                    handler.removeCallbacks(rKhachHang);
                    handler.postDelayed(rKhachHang, TIME_INTERVAL);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "KhachHang - onChildChanged: " + dataSnapshot.getKey());
                KhachHang khachHang = dataSnapshot.getValue(KhachHang.class);
                DBController.getInstance(mContext).capNhatKhachHangDenCache(khachHang);
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
                DBController.getInstance(mContext).capNhatHoacThemSanPhamDenCache(sanPham);
                if (isLoadSanPhamComplete) {
                    SanPhamEvent event = new SanPhamEvent();
                    event.setSanPham(sanPham);
                    EventBus.getDefault().post(event);
                } else {
                    handler.removeCallbacks(rSanPham);
                    handler.postDelayed(rSanPham, TIME_INTERVAL);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                LogUtils.d(TAG, "SanPham - onChildChanged: " + dataSnapshot.getKey());
                SanPham sanPham = dataSnapshot.getValue(SanPham.class);
                DBController.getInstance(mContext).capNhatSanPhamDenCache(sanPham);
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
                Boolean connected = snapshot.getValue(Boolean.class);
                NetStatusEvent event = new NetStatusEvent();
                event.setConnect(connected != null && connected);
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
        handler.postDelayed(rDonHang, TIME_INTERVAL);
        handler.postDelayed(rKhachHang, TIME_INTERVAL);
        handler.postDelayed(rSanPham, TIME_INTERVAL);
    }

    private void stopLoad() {
        if (isLoadDonHangComplete && isLoadKhachHangComplete && isLoadSanPhamComplete) {
            LogUtils.d(TAG, "stopLoad");
            LoadCompleteEvent event = new LoadCompleteEvent();
            EventBus.getDefault().post(event);
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

    private String getUid() {
        return Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    }

    public void taiDonHangLenServer(List<DonHang> donHangs) {
        LogUtils.d(TAG, "taiDonHangLenServer");
        for (DonHang donHang : donHangs)
            themDonHang(donHang);
    }

    public void taiKhachHangLenServer(List<KhachHang> khachHangs) {
        LogUtils.d(TAG, "taiKhachHangLenServer");
        for (KhachHang khachHang : khachHangs)
            themKhachHang(khachHang);
    }

    public void taiSanPhamLenServer(List<SanPham> sanPhams) {
        LogUtils.d(TAG, "taiSanPhamLenServer");
        for (SanPham sanPham : sanPhams)
            themSanPham(sanPham);
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

    public void themNguoiDung(User user) {
        mUserDatabase.child(user.getId()).setValue(user);
    }
}

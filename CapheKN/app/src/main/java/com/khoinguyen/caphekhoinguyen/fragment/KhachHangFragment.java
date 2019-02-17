package com.khoinguyen.caphekhoinguyen.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.adapter.KhachHangAdapter;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.event.KhachHangEvent;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.realtimedatabase.RealtimeDatabaseController;
import com.khoinguyen.caphekhoinguyen.utils.Constants;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;
import com.khoinguyen.caphekhoinguyen.utils.Utils;
import com.melnykov.fab.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class KhachHangFragment extends Fragment {
    private static final String TAG = "KhachHangFragment";

    private OnKhachHangInteractionListener mListener;

    private List<KhachHang> mKhachHangs;
    private RecyclerView mRecyclerView;
    private KhachHangAdapter mAdapter;
    private DBController dbController;

    public KhachHangFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khach_hang, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvKhachHang);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themKhachHang();
            }
        });
        fab.attachToRecyclerView(mRecyclerView);

        dbController = DBController.getInstance(getActivity());
        return view;
    }

    private void themKhachHang() {
        final KhachHang khachHang = new KhachHang();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thêm khách hàng");
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_them_khach_hang, null);

        final EditText etTenKhachHang = (EditText) view.findViewById(R.id.etTenKhachHang);
        final EditText etSoDienThoai = (EditText) view.findViewById(R.id.etSoDienThoai);

        builder.setView(view);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(etTenKhachHang.getText().toString().trim())) {
                    khachHang.setId(RealtimeDatabaseController.getInstance().genKeyKhachHang());
                    khachHang.setTenKH(etTenKhachHang.getText().toString().trim());
                    khachHang.setSDT(etSoDienThoai.getText().toString().trim());
                    dbController.themKhachHang(khachHang);
                    mKhachHangs.add(khachHang);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(getActivity(), "Vui lòng nhập tên khách hàng");
                }
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mAdapter) {
            mRecyclerView.setAdapter(mAdapter);
        } else {
            getKhachHangs();
        }
    }

    private void getKhachHangs() {
        mKhachHangs = dbController.layDanhSachKhachHang();
        mAdapter = new KhachHangAdapter(getActivity(), mKhachHangs, Constants.TRANG_THAI_DANG_XY_LY, true, mListener);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(KhachHangEvent event) {
        LogUtils.d(TAG, "onMessageEvent: " + event.getKhachHang().getId());
        getKhachHangs();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TrangChuFragment.OnTrangChuInteractionListener) {
            mListener = (OnKhachHangInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOrderInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnKhachHangInteractionListener {
        void onKhachHangInteraction(String idKhachHang, int trangThai);
    }
}

package com.khoinguyen.caphekhoinguyen.fragment;


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
import com.khoinguyen.caphekhoinguyen.adapter.SanPhamAdapter;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.event.SanPhamEvent;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.realtimedatabase.RealtimeDatabaseController;
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
public class SanPhamFragment extends Fragment {
    private static final String TAG = "SanPhamFragment";

    private List<SanPham> mSanPhams;
    private RecyclerView mRecyclerView;
    private SanPhamAdapter mAdapter;

    public SanPhamFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_san_pham, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvSanPham);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themSanPham();
            }
        });
        fab.attachToRecyclerView(mRecyclerView);

        return view;
    }

    private void themSanPham() {
        final SanPham sanPham = new SanPham();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thêm sản phẩm");
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_them_san_pham, null);

        final EditText etTenSanPham = (EditText) view.findViewById(R.id.etTenSanPham);
        final EditText etDonGia = (EditText) view.findViewById(R.id.etDonGia);

        builder.setView(view);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!TextUtils.isEmpty(etTenSanPham.getText().toString().trim()) && !TextUtils.isEmpty(etDonGia.getText().toString().trim())) {
                    sanPham.setId(RealtimeDatabaseController.getInstance(getActivity()).genKeySanPham());
                    sanPham.setTenSP(etTenSanPham.getText().toString().trim());
                    sanPham.setDonGia(Long.valueOf(etDonGia.getText().toString().trim()));
                    DBController.getInstance(getActivity()).themSanPham(sanPham);
                    mSanPhams.add(sanPham);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(getActivity(), "Vui lòng nhập tên và giá sản phẩm");
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
            getSanPhams();
        }
    }

    private void getSanPhams() {
        mSanPhams = DBController.getInstance(getActivity()).layDanhSachSanPham();
        mAdapter = new SanPhamAdapter(getActivity(), mSanPhams);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SanPhamEvent event) {
        LogUtils.d(TAG, "onMessageEvent: " + event.getSanPham().getId());
        getSanPhams();
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
}

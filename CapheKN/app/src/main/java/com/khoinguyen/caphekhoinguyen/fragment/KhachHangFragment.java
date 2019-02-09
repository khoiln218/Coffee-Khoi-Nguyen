package com.khoinguyen.caphekhoinguyen.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.adapter.KhachHangAdapter;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class KhachHangFragment extends Fragment {
    private static final String TAG = "KhachHangFragment";

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
        View view = inflater.inflate(R.layout.fragment_ban_hang, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themKhachHang();
            }
        });

        dbController = new DBController(getActivity());
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
                khachHang.setTenKH(etTenKhachHang.getText().toString());
                khachHang.setSDT(etSoDienThoai.getText().toString());
                dbController.themKhachHang(khachHang);
                mKhachHangs.add(khachHang);
                mAdapter.notifyDataSetChanged();
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
        mAdapter = new KhachHangAdapter(getActivity(), mKhachHangs);
        mRecyclerView.setAdapter(mAdapter);
    }
}

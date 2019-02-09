package com.khoinguyen.caphekhoinguyen.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.adapter.DonHangAdapter;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BanHangFragment extends Fragment {
    private static final String TAG = "BanHangFragment";

    private List<DonHang> mDonHangs;
    private RecyclerView mRecyclerView;
    private DonHangAdapter mAdapter;
    private DBController dbController;

    public BanHangFragment() {
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
                banHang();
            }
        });

        dbController = new DBController(getActivity());
        return view;
    }

    private void banHang() {
        final DonHang donHang = new DonHang();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Bán hàng");
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_them_don_hang, null);

        final List<KhachHang> khachHangs = dbController.layDanhSachKhachHang();
        List<String> dsKhachHang = new ArrayList<>();
        for (KhachHang kh : khachHangs) {
            dsKhachHang.add(kh.getTenKH());
        }
        ArrayAdapter<String> adapterKH = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, dsKhachHang);
        AutoCompleteTextView etKhachHang = (AutoCompleteTextView) view.findViewById(R.id.etKhachHang);
        etKhachHang.setThreshold(1);
        etKhachHang.setAdapter(adapterKH);
        etKhachHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tenKH = parent.getItemAtPosition(position).toString();
                for (KhachHang kh : khachHangs) {
                    if (TextUtils.equals(kh.getTenKH(), tenKH)) {
                        donHang.setKhachHang(kh);
                        break;
                    }
                }
            }
        });

        final List<SanPham> sanPhams = dbController.layDanhSachSanPham();
        List<String> dsSanPham = new ArrayList<>();
        for (SanPham sp : sanPhams) {
            dsSanPham.add(sp.getTenSP());
        }
        ArrayAdapter<String> adapterSP = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, dsSanPham);
        AutoCompleteTextView etSanPham = (AutoCompleteTextView) view.findViewById(R.id.etSanPham);
        etSanPham.setThreshold(1);
        etSanPham.setAdapter(adapterSP);
        etSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String tenSP = parent.getItemAtPosition(position).toString();
                for (SanPham sp : sanPhams) {
                    if (TextUtils.equals(sp.getTenSP(), tenSP)) {
                        donHang.setSanPham(sp);
                        break;
                    }
                }
            }
        });

        builder.setView(view);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                donHang.setThoiGianTao(System.currentTimeMillis());
                donHang.setTrangThai(getString(R.string.status_dang_xu_ly));
                dbController.themDonHang(donHang);
                mDonHangs.add(donHang);
                mAdapter.setData();
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
            getDonHangs();
        }
    }

    private void getDonHangs() {
        mDonHangs = dbController.layDanhSachDonHang();
        mAdapter = new DonHangAdapter(getActivity(), mDonHangs);
        mRecyclerView.setAdapter(mAdapter);
    }
}

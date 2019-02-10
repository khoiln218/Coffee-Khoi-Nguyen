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
import android.widget.AutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.adapter.DonHangAdapter;
import com.khoinguyen.caphekhoinguyen.adapter.KhachHangArrayAdapter;
import com.khoinguyen.caphekhoinguyen.adapter.SanPhamArrayAdapter;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;

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
        KhachHangArrayAdapter adapterKH = new KhachHangArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, khachHangs);
        AutoCompleteTextView etKhachHang = (AutoCompleteTextView) view.findViewById(R.id.etKhachHang);
        etKhachHang.setThreshold(1);
        etKhachHang.setAdapter(adapterKH);
        etKhachHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KhachHang khachHang = (KhachHang) parent.getItemAtPosition(position);
                donHang.setKhachHang(khachHang);
            }
        });

        final List<SanPham> sanPhams = dbController.layDanhSachSanPham();
        SanPhamArrayAdapter adapterSP = new SanPhamArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, sanPhams);
        final MultiAutoCompleteTextView etSanPham = (MultiAutoCompleteTextView) view.findViewById(R.id.etSanPham);
        etSanPham.setThreshold(1);
        etSanPham.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        etSanPham.setAdapter(adapterSP);

        builder.setView(view);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                donHang.setThoiGianTao(System.currentTimeMillis());
                donHang.setTrangThai(getString(R.string.status_dang_xu_ly));
                String[] tenSPs = etSanPham.getText().toString().split(",");
                for (String tenSP : tenSPs) {
                    for (SanPham sanPham : sanPhams) {
                        if (TextUtils.equals(tenSP, sanPham.getTenSP())) {
                            donHang.addSanPham(sanPham);
                            break;
                        }
                    }
                }
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

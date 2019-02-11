package com.khoinguyen.caphekhoinguyen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.fragment.KhachHangFragment;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> {
    private final List<KhachHang> mValues;
    private Context mContext;
    private DBController dbController;
    private KhachHangFragment.OnKhachHangInteractionListener mListener;

    public KhachHangAdapter(Context context, List<KhachHang> items, KhachHangFragment.OnKhachHangInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
        dbController = new DBController(context);
    }

    @NonNull
    @Override
    public KhachHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.khach_hang_item, parent, false);
        return new KhachHangAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTvTen.setText(String.format(Locale.US, "%d.", (position + 1)) + holder.mItem.getTenKH());
        String formattedPrice = new DecimalFormat("##,##0VNĐ").format(getTongTien(holder.mItem.getId()));
        holder.mTvTongTien.setText(formattedPrice);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onKhachHangInteraction(holder.mItem.getId());
            }
        });
    }

    private long getTongTien(int id) {
        List<DonHang> donHangs = dbController.layDonHangDangXuLyTheoKhachHang(id);
        long tongTien = 0;
        for (DonHang donHang : donHangs) {
            for (SanPham sanPham : donHang.getSanPhams()) {
                tongTien += sanPham.getDonGia();
            }
        }
        return tongTien;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTvTen;
        final TextView mTvTongTien;
        KhachHang mItem;

        public ViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            mTvTen = (TextView) view.findViewById(R.id.tvTen);
            mTvTongTien = (TextView) view.findViewById(R.id.tvTongTien);
        }
    }
}

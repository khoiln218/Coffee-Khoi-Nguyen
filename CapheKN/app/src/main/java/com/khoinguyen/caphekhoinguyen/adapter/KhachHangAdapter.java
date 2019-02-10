package com.khoinguyen.caphekhoinguyen.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import java.util.List;
import java.util.Locale;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> {
    private final List<KhachHang> mValues;
    private Context mContext;

    public KhachHangAdapter(Context context, List<KhachHang> items) {
        mContext = context;
        mValues = items;
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
        holder.mTvTongTien.setText("0VND");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(mContext, "Click name: " + holder.mItem.getTenKH());
            }
        });
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

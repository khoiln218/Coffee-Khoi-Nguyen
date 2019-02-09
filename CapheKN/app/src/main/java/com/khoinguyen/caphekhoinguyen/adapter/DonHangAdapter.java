package com.khoinguyen.caphekhoinguyen.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.fragment.BanHangFragment;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {

    private final List<DonHang> mValues;
    private List<DonHang> mValuesFilter;
    private Context mContext;

    public DonHangAdapter(Context context, List<DonHang> items) {
        mValues = items;
        mContext = context;

        setData();
    }

    private void setData() {
        mValuesFilter = new ArrayList<>();
        for (DonHang order : mValues) {
            if (!TextUtils.equals(order.getTrangThai(), mContext.getString(R.string.status_da_huy))) {
                mValuesFilter.add(order);
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.don_hang_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValuesFilter.get(position);
        Resources resources = holder.mView.getResources();
        String thoiGian = String.format(Locale.US, "%d. ", (position + 1)) + Utils.convTimestamp(holder.mItem.getThoiGianTao());
        holder.mTvThoiGianTao.setText(thoiGian);
        if (holder.mItem.getKhachHang() != null) {
            holder.mTvKhachHang.setText(holder.mItem.getKhachHang().getTenKH());
        } else {
            holder.mTvKhachHang.setText("Khách vãng lai");
        }
        holder.mTvSanPham.setText(holder.mItem.getSanPham().getTenSP());

        boolean isTracking = TextUtils.equals(holder.mItem.getTrangThai(), resources.getString(R.string.status_dang_xu_ly));
        holder.mIvTrangThai.setImageResource(isTracking ? R.drawable.orderlist_07 : R.drawable.orderlist_08);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        holder.btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValuesFilter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTvThoiGianTao;
        final ImageView mIvTrangThai;
        final TextView mTvKhachHang;
        final TextView mTvSanPham;
        final ImageButton btnThanhToan;
        final ImageButton btnChinhSua;
        DonHang mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvThoiGianTao = (TextView) view.findViewById(R.id.tvThoiGianTao);
            mIvTrangThai = (ImageView) view.findViewById(R.id.trangThai);
            mTvKhachHang = (TextView) view.findViewById(R.id.tvKhachHang);
            mTvSanPham = (TextView) view.findViewById(R.id.tvSanPham);
            btnThanhToan = (ImageButton) view.findViewById(R.id.btnThanhToan);
            btnChinhSua = (ImageButton) view.findViewById(R.id.btnChinhSua);
        }
    }
}

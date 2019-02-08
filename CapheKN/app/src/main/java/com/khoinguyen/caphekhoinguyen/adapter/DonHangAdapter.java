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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {

    private final List<DonHang> mValues;
    private List<DonHang> mValuesFilter;
    private final BanHangFragment.OnOrderInteractionListener mListener;
    private Context mContext;

    public DonHangAdapter(Context context, List<DonHang> items, BanHangFragment.OnOrderInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
                .inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValuesFilter.get(position);
        Resources resources = holder.mView.getResources();
        holder.mTvOrderId.setText(holder.mItem.getThoiGianTao() + "");

        boolean isTracking = TextUtils.equals(holder.mItem.getTrangThai(), resources.getString(R.string.status_dang_xu_ly));
        holder.mIvStatus.setImageResource(isTracking ? R.drawable.orderlist_07 : R.drawable.orderlist_08);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onOrderItemSelected(holder.mItem, v.getId());
                }
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onOrderItemSelected(holder.mItem, v.getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValuesFilter.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTvOrderId;
        final ImageView mIvStatus;
        final ImageButton btnLocation, btnEdit;
        DonHang mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvOrderId = (TextView) view.findViewById(R.id.tvMaDonHang);
            mIvStatus = (ImageView) view.findViewById(R.id.status);
            btnLocation = (ImageButton) view.findViewById(R.id.btnLocation);
            btnEdit = (ImageButton) view.findViewById(R.id.btnEdit);
        }
    }
}

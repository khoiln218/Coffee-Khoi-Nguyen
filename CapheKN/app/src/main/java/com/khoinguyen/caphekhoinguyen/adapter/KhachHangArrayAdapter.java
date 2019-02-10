package com.khoinguyen.caphekhoinguyen.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.model.KhachHang;

import java.util.ArrayList;
import java.util.List;

public class KhachHangArrayAdapter extends ArrayAdapter<KhachHang> {
    private final Context mContext;
    private final List<KhachHang> mKhachHangs;
    private final List<KhachHang> mKhachHangsAll;
    private final int mLayoutResourceId;

    public KhachHangArrayAdapter(Context context, int resource, List<KhachHang> items) {
        super(context, resource, items);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mKhachHangs = new ArrayList<>(items);
        this.mKhachHangsAll = new ArrayList<>(items);
    }

    public int getCount() {
        return mKhachHangs.size();
    }

    public KhachHang getItem(int position) {
        return mKhachHangs.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        try {
            if (view == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                view = inflater.inflate(mLayoutResourceId, parent, false);
            }
            KhachHang khachHang = getItem(position);
            TextView name = (TextView) view.findViewById(android.R.id.text1);
            name.setText(khachHang.getTenKH());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((KhachHang) resultValue).getTenKH();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<KhachHang> khachHangsSuggestion = new ArrayList<>();
                if (constraint != null) {
                    for (KhachHang khachHang : mKhachHangsAll) {
                        if (khachHang.getTenKH().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            khachHangsSuggestion.add(khachHang);
                        }
                    }
                    filterResults.values = khachHangsSuggestion;
                    filterResults.count = khachHangsSuggestion.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mKhachHangs.clear();
                if (results != null && results.count > 0) {
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof KhachHang) {
                            mKhachHangs.add((KhachHang) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    mKhachHangs.addAll(mKhachHangsAll);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}

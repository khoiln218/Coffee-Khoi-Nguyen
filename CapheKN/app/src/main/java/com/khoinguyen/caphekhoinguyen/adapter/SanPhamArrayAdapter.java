package com.khoinguyen.caphekhoinguyen.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class SanPhamArrayAdapter extends ArrayAdapter<SanPham> {
    private final Context mContext;
    private final List<SanPham> mSanPhams;
    private final List<SanPham> mSanPhamsAll;
    private final int mLayoutResourceId;

    public SanPhamArrayAdapter(Context context, int resource, List<SanPham> items) {
        super(context, resource, items);
        this.mContext = context;
        this.mLayoutResourceId = resource;
        this.mSanPhams = new ArrayList<>(items);
        this.mSanPhamsAll = new ArrayList<>(items);
    }

    public int getCount() {
        return mSanPhams.size();
    }

    public SanPham getItem(int position) {
        return mSanPhams.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        try {
            if (view == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                view = inflater.inflate(mLayoutResourceId, parent, false);
            }
            SanPham sanPham = getItem(position);
            TextView name = view.findViewById(android.R.id.text1);
            name.setText(sanPham.getTenSP());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((SanPham) resultValue).getTenSP();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<SanPham> sanPhamsSuggestion = new ArrayList<>();
                if (constraint != null) {
                    for (SanPham sanPham : mSanPhamsAll) {
                        if (sanPham.getTenSP().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            sanPhamsSuggestion.add(sanPham);
                        }
                    }
                    filterResults.values = sanPhamsSuggestion;
                    filterResults.count = sanPhamsSuggestion.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mSanPhams.clear();
                if (results != null && results.count > 0) {
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof SanPham) {
                            mSanPhams.add((SanPham) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    mSanPhams.addAll(mSanPhamsAll);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}

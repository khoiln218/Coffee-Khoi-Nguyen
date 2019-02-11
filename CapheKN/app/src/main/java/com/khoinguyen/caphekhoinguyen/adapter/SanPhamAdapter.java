package com.khoinguyen.caphekhoinguyen.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.model.SanPham;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ViewHolder> {
    private final List<SanPham> mValues;
    private Context mContext;
    private DBController dbController;

    public SanPhamAdapter(Context context, List<SanPham> items) {
        this.mContext = context;
        this.mValues = items;
        dbController = new DBController(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.san_pham_item, parent, false);
        return new SanPhamAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mTvTen.setText(String.format(Locale.US, "%d.", (position + 1)) + holder.mItem.getTenSP());
        String formattedPrice = new DecimalFormat("##,##0VNĐ").format(holder.mItem.getDonGia());
        holder.mTvDonGia.setText(formattedPrice);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu(holder.mItem, holder.mView);
            }
        });
    }

    private void openMenu(final SanPham sanPham, View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        popup.inflate(R.menu.menu_san_pham);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.option_chinh_sua:
                        chinhSua(sanPham);
                        break;
                    case R.id.option_huy:
                        huy(sanPham);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void chinhSua(final SanPham sp) {
        final SanPham sanPham = new SanPham();
        sanPham.setId(sp.getId());
        sanPham.setTenSP(sp.getTenSP());
        sanPham.setDonGia(sp.getDonGia());

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setTitle("Chỉnh sửa sản phẩm");
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_them_san_pham, null);

        final EditText etTenSanPham = (EditText) view.findViewById(R.id.etTenSanPham);
        final EditText etDonGia = (EditText) view.findViewById(R.id.etDonGia);

        etTenSanPham.setText(sanPham.getTenSP());
        etTenSanPham.requestFocus();
        etDonGia.setText(String.valueOf(sanPham.getDonGia()));

        builder.setView(view);

        builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sanPham.setTenSP(etTenSanPham.getText().toString());
                sanPham.setDonGia(Long.valueOf(etDonGia.getText().toString().trim()));
                dbController.capNhatSanPham(sanPham);
                mValues.set(mValues.indexOf(sp), sanPham);
                notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void huy(final SanPham sanPham) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Xóa sản phẩm này?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mValues.remove(sanPham);
                        dbController.xoaSanPham(sanPham.getId());
                        notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTvTen;
        final TextView mTvDonGia;
        SanPham mItem;

        public ViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            mTvTen = (TextView) view.findViewById(R.id.tvTen);
            mTvDonGia = (TextView) view.findViewById(R.id.tvDonGia);
        }
    }
}

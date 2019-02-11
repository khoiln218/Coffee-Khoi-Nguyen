package com.khoinguyen.caphekhoinguyen.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
                openMenu(holder.mItem, holder.mView);
            }
        });
    }

    private void openMenu(final KhachHang khachHang, View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        popup.inflate(R.menu.menu_khach_hang);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.option_chinh_sua:
                        chinhSua(khachHang);
                        break;
                    case R.id.option_lich_su:
                        lichSuGiaoDich(khachHang);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void chinhSua(final KhachHang kh) {
        final KhachHang khachHang = new KhachHang();
        khachHang.setId(kh.getId());
        khachHang.setTenKH(kh.getTenKH());
        khachHang.setSDT(kh.getSDT());

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Chỉnh sửa khách hàng");
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_them_khach_hang, null);

        final EditText etTenKhachHang = (EditText) view.findViewById(R.id.etTenKhachHang);
        final EditText etSoDienThoai = (EditText) view.findViewById(R.id.etSoDienThoai);

        etTenKhachHang.setText(khachHang.getTenKH());
        etTenKhachHang.requestFocus();
        etSoDienThoai.setText(khachHang.getSDT());

        builder.setView(view);

        builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                khachHang.setTenKH(etTenKhachHang.getText().toString());
                khachHang.setSDT(etSoDienThoai.getText().toString());
                dbController.capNhatKhachHang(khachHang);
                mValues.set(mValues.indexOf(kh), khachHang);
                notifyDataSetChanged();
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

    private void lichSuGiaoDich(KhachHang khachHang) {
        mListener.onKhachHangInteraction(khachHang.getId());
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

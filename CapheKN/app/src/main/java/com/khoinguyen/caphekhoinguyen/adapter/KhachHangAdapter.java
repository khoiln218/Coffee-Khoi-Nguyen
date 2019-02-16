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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.database.SanPhamHandler;
import com.khoinguyen.caphekhoinguyen.fragment.KhachHangFragment;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.utils.Constants;

import java.text.DecimalFormat;
import java.util.List;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> {
    private final List<KhachHang> mValues;
    private Context mContext;
    private DBController dbController;
    private KhachHangFragment.OnKhachHangInteractionListener mListener;
    private int mTrangThai;
    private boolean mIsChinhSua;

    public KhachHangAdapter(Context context, List<KhachHang> items, int trangThai, boolean isChinhSua, KhachHangFragment.OnKhachHangInteractionListener listener) {
        mContext = context;
        mValues = items;
        mTrangThai = trangThai;
        mIsChinhSua = isChinhSua;
        mListener = listener;
        dbController = DBController.getInstance(context);
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
        TextDrawable drawable = TextDrawable.builder()
                .round().build(String.valueOf(holder.mItem.getTenKH().charAt(0)), ColorGenerator.MATERIAL.getColor(holder.mItem.getTenKH()));
        holder.imageView.setImageDrawable(drawable);
        holder.mTvTen.setText(holder.mItem.getTenKH());
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
        popup.inflate(mIsChinhSua ? R.menu.menu_khach_hang : R.menu.menu_khach_hang_2);
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
        mListener.onKhachHangInteraction(khachHang.getId(), mTrangThai);
    }

    private long getTongTien(String id) {
        List<DonHang> donHangs = mTrangThai == Constants.TRANG_THAI_HOAN_THANH ? dbController.layDonHangHoanThanhTheoKhachHang(id) : dbController.layDonHangDangXuLyTheoKhachHang(id);
        long tongTien = 0;
        for (DonHang donHang : donHangs) {
            for (String idSanPham : donHang.getIdSanPhams()) {
                tongTien += SanPhamHandler.getInstance(mContext).getSanPhamById(idSanPham).getDonGia();
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
        final ImageView imageView;
        final TextView mTvTen;
        final TextView mTvTongTien;
        KhachHang mItem;

        public ViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            imageView = (ImageView) view.findViewById(R.id.ivIcon);
            mTvTen = (TextView) view.findViewById(R.id.tvTen);
            mTvTongTien = (TextView) view.findViewById(R.id.tvTongTien);
        }
    }
}

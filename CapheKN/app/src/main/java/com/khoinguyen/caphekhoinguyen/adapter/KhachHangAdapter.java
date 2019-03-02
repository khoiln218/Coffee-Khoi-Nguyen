package com.khoinguyen.caphekhoinguyen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
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
import com.khoinguyen.caphekhoinguyen.fragment.KhachHangFragment;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.utils.Constants;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.ViewHolder> {
    private final List<KhachHang> mValues;
    private Context mContext;
    private KhachHangFragment.OnKhachHangInteractionListener mListener;
    private int mTrangThai;
    private boolean mIsChinhSua;

    public KhachHangAdapter(Context context, List<KhachHang> items, int trangThai, boolean isChinhSua, KhachHangFragment.OnKhachHangInteractionListener listener) {
        mContext = context;
        mValues = items;
        mTrangThai = trangThai;
        mIsChinhSua = isChinhSua;
        mListener = listener;
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
        holder.ivIcon.setImageDrawable(drawable);
        holder.mTvTen.setText(holder.mItem.getTenKH());
        long tongTien = getTongTien(holder.mItem.getId());
        if (tongTien > 0) {
            String formattedPrice = new DecimalFormat("##,##0VNĐ").format(tongTien);
            holder.mTvTongTien.setVisibility(View.VISIBLE);
            holder.mTvTongTien.setText(formattedPrice);
        } else {
            holder.mTvTongTien.setVisibility(View.GONE);
        }

        holder.mView.setOnClickListener(v -> openMenu(holder.mItem, holder.mView));
    }

    private void openMenu(final KhachHang khachHang, View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        popup.inflate(mIsChinhSua ? R.menu.menu_khach_hang : R.menu.menu_khach_hang_2);
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.option_chinh_sua:
                    chinhSua(khachHang);
                    break;
                case R.id.option_lich_su:
                    lichSuGiaoDich(khachHang);
                    break;
            }
            return false;
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
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_them_khach_hang, null);

        final EditText etTenKhachHang = view.findViewById(R.id.etTenKhachHang);
        final EditText etSoDienThoai = view.findViewById(R.id.etSoDienThoai);

        etTenKhachHang.setText(khachHang.getTenKH());
        etTenKhachHang.requestFocus();
        etSoDienThoai.setText(khachHang.getSDT());

        builder.setView(view);

        builder.setPositiveButton("Sửa", (dialog, which) -> {
            if (TextUtils.isEmpty(etTenKhachHang.getText().toString().trim())) {
                Utils.showToast(mContext, "Sửa thất bại. Vui lòng nhập tên khách hàng");
            } else if (DBController.getInstance(mContext).layKhachHangTheoTen(etTenKhachHang.getText().toString().trim()) != null) {
                Utils.showToast(mContext, "Sửa thất bại. Bị trùng tên khách hàng");
            } else {
                khachHang.setTenKH(etTenKhachHang.getText().toString());
                khachHang.setSDT(etSoDienThoai.getText().toString());
                DBController.getInstance(mContext).capNhatKhachHang(khachHang);
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void lichSuGiaoDich(KhachHang khachHang) {
        mListener.onKhachHangInteraction(khachHang.getId(), mTrangThai);
    }

    private long getTongTien(String id) {
        List<DonHang> donHangs = mTrangThai == Constants.TRANG_THAI_HOAN_THANH ? DBController.getInstance(mContext).layDonHangHoanThanhTheoKhachHang(id) : DBController.getInstance(mContext).layDonHangDangXuLyTheoKhachHang(id);
        long tongTien = 0;
        for (DonHang donHang : donHangs) {
            tongTien += donHang.getTongTien(mContext);
        }
        return tongTien;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final ImageView ivIcon;
        final TextView mTvTen;
        final TextView mTvTongTien;
        KhachHang mItem;

        ViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            ivIcon = view.findViewById(R.id.ivIcon);
            mTvTen = view.findViewById(R.id.tvTen);
            mTvTongTien = view.findViewById(R.id.tvTongTien);
        }
    }
}

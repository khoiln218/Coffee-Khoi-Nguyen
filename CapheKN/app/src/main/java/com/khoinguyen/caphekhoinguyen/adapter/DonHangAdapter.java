package com.khoinguyen.caphekhoinguyen.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.database.KhachHangHandler;
import com.khoinguyen.caphekhoinguyen.database.SanPhamHandler;
import com.khoinguyen.caphekhoinguyen.fragment.BanHangFragment;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.utils.Constants;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import me.next.tagview.TagCloudView;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {

    private List<DonHang> mValues;
    private Context mContext;
    private BanHangFragment.OnDonHangListerner mDonHangListerner;
    private BanHangFragment.OnBanHangInteractionListener mBanHangListerner;
    private boolean mIsLichSuGiaoDich;
    private int mTrangThai;
    private DBController dbController;
    private AlertDialog alertDialog;
    private SparseBooleanArray itemStateArray = new SparseBooleanArray();

    public DonHangAdapter(Context context, List<DonHang> items, BanHangFragment.OnDonHangListerner donHangListerner, BanHangFragment.OnBanHangInteractionListener banHangListener) {
        mContext = context;
        mValues = items;
        mDonHangListerner = donHangListerner;
        mBanHangListerner = banHangListener;
        mIsLichSuGiaoDich = false;
        mTrangThai = Constants.TRANG_THAI_DANG_XY_LY;
        dbController = DBController.getInstance(context);
    }

    public DonHangAdapter(Context context, List<DonHang> items, BanHangFragment.OnDonHangListerner donHangListerner, BanHangFragment.OnBanHangInteractionListener banHangListener, boolean isLichSuGiaoDich, int trangThai) {
        mContext = context;
        mValues = items;
        mDonHangListerner = donHangListerner;
        mBanHangListerner = banHangListener;
        mIsLichSuGiaoDich = isLichSuGiaoDich;
        mTrangThai = trangThai;
        dbController = DBController.getInstance(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.don_hang_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mTvThoiGianTao.setText(Utils.convTimestamp(holder.mItem.getThoiGianTao(), "hh:mm:ss"));
        if (holder.mItem.getIdKhachHang() != null) {
            KhachHang khachHang = KhachHangHandler.getInstance(mContext).getKhachHangById(holder.mItem.getIdKhachHang());
            holder.mTvKhachHang.setText(khachHang.getTenKH());
            TextDrawable drawable = TextDrawable.builder()
                    .round().build(String.valueOf(khachHang.getTenKH().charAt(0)), ColorGenerator.MATERIAL.getColor(khachHang.getTenKH()));
            holder.ivIcon.setImageDrawable(drawable);
        } else {
            holder.mTvKhachHang.setText("Khách vãng lai");
            TextDrawable drawable = TextDrawable.builder()
                    .round().build("K", ColorGenerator.MATERIAL.getColor("Khách vãng lai"));
            holder.ivIcon.setImageDrawable(drawable);
        }
        List<SanPham> sanPhams = new ArrayList<>();
        for (String id : holder.mItem.getIdSanPhams())
            sanPhams.add(SanPhamHandler.getInstance(mContext).getSanPhamById(id));
        if (sanPhams != null) {
            holder.tcvSanPham.setTags(getTagSanPhams(sanPhams));
            String formattedPrice = new DecimalFormat("##,##0VNĐ").format(holder.mItem.getTongTien(mContext));
            holder.mTvTongTien.setText(formattedPrice);
        }

        holder.mView.setBackgroundColor(itemStateArray.get(position, false) ? ContextCompat.getColor(mContext, R.color.colorAccent) : Color.WHITE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTrangThai == Constants.TRANG_THAI_DANG_XY_LY) {
                    if (!itemStateArray.get(position, false)) {
                        itemStateArray.put(position, true);
                        holder.mView.setBackgroundColor(0x999be6ff);
                        mDonHangListerner.onShow();
                        selectChange();
                    } else {
                        itemStateArray.delete(position);
                        holder.mView.setBackgroundColor(Color.TRANSPARENT);
                        if (itemStateArray.size() == 0)
                            mDonHangListerner.onHide();
                        else mDonHangListerner.onShow();
                        selectChange();
                    }
                } else if (mTrangThai == Constants.TRANG_THAI_HOAN_THANH) {
                    PopupMenu popup = new PopupMenu(mContext, holder.mView);
                    popup.inflate(R.menu.menu_don_hang_hoan_thanh);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.option_huy:
                                    huy(holder.mItem);
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            }
        });

        holder.btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thanhToan(holder.mItem);
            }
        });

        holder.btnChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chinhSua(holder.mItem, holder.btnChinhSua);
            }
        });

        if (!TextUtils.equals(holder.mItem.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))) {
            holder.mSeparateLine.setVisibility(View.GONE);
            holder.mLayoutChinhSua.setVisibility(View.GONE);
        }
    }

    private void selectChange() {
        mDonHangListerner.onUpdateTongTien(tongTien());
    }

    public void clearSelect() {
        itemStateArray.clear();
        mDonHangListerner.onHide();
        notifyDataSetChanged();
        selectChange();
    }

    private void chinhSua(final DonHang donHang, View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        popup.inflate(R.menu.menu_don_hang);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.option_chinh_sua:
                        capNhat(donHang);
                        break;
                    case R.id.option_huy:
                        huy(donHang);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void huy(final DonHang donHang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Hủy đơn hàng?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DonHang dh = donHang;
                        donHang.setTrangThai(mContext.getString(R.string.status_da_huy));
                        mValues.remove(dh);
                        dbController.capNhatDonHang(dh);
                        clearSelect();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearSelect();
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void capNhat(final DonHang dh) {
        final DonHang donHang = new DonHang();
        donHang.setId(dh.getId());
        donHang.setThoiGianTao(dh.getThoiGianTao());
        donHang.setTrangThai(dh.getTrangThai());
        donHang.setIdKhachHang(dh.getIdKhachHang());

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Chỉnh sửa đơn hàng");
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_them_don_hang, null);

        final List<KhachHang> khachHangs = dbController.layDanhSachKhachHang();
        KhachHangArrayAdapter adapterKH = new KhachHangArrayAdapter(mContext, android.R.layout.simple_dropdown_item_1line, khachHangs);
        AutoCompleteTextView etKhachHang = (AutoCompleteTextView) view.findViewById(R.id.etKhachHang);
        etKhachHang.setThreshold(1);
        etKhachHang.setAdapter(adapterKH);
        if (!TextUtils.isEmpty(donHang.getIdKhachHang()))
            etKhachHang.setText(KhachHangHandler.getInstance(mContext).getKhachHangById(donHang.getIdKhachHang()).getTenKH());
        else
            etKhachHang.setEnabled(false);
        etKhachHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KhachHang khachHang = (KhachHang) parent.getItemAtPosition(position);
                donHang.setIdKhachHang(khachHang.getId());
            }
        });

        final List<SanPham> sanPhams = dbController.layDanhSachSanPham();
        SanPhamArrayAdapter adapterSP = new SanPhamArrayAdapter(mContext, android.R.layout.simple_dropdown_item_1line, sanPhams);
        final MultiAutoCompleteTextView etSanPham = (MultiAutoCompleteTextView) view.findViewById(R.id.etSanPham);
        etSanPham.setThreshold(1);
        etSanPham.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        etSanPham.setAdapter(adapterSP);
        String sanPhamString = "";
        for (String id : dh.getIdSanPhams()) {
            sanPhamString += SanPhamHandler.getInstance(mContext).getSanPhamById(id).getTenSP() + ",";
        }
        etSanPham.setText(sanPhamString);
        etSanPham.requestFocus();

        final Button btnThemKhachHang = (Button) view.findViewById(R.id.btnThemKhachHang);
        btnThemKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBanHangListerner != null)
                    mBanHangListerner.onThemKhachHangClick();
                alertDialog.cancel();
            }
        });

        final Button btnThemSanPham = (Button) view.findViewById(R.id.btnThemSanPham);
        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBanHangListerner != null)
                    mBanHangListerner.onThemSanPhamClick();
                alertDialog.cancel();
            }
        });

        builder.setView(view);

        builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] tenSPs = etSanPham.getText().toString().split(",");
                for (String tenSP : tenSPs) {
                    for (SanPham sanPham : sanPhams) {
                        if (TextUtils.equals(tenSP.trim(), sanPham.getTenSP())) {
                            donHang.addSanPham(sanPham);
                            break;
                        }
                    }
                }
                if (donHang.getIdSanPhams() != null) {
                    dbController.capNhatDonHang(donHang);
                    if (mIsLichSuGiaoDich && !TextUtils.equals(donHang.getIdKhachHang(), dh.getIdKhachHang())) {
                        mValues.remove(dh);
                    } else
                        mValues.set(mValues.indexOf(dh), donHang);
                } else {
                    Utils.showToast(mContext, "Chỉnh sửa đơn hàng thất bại");
                }
                clearSelect();
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearSelect();
                dialog.cancel();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void thanhToan(final DonHang dh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Thanh toán đơn hàng?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DonHang donHang = new DonHang();
                        donHang.setId(dh.getId());
                        donHang.setThoiGianTao(dh.getThoiGianTao());
                        donHang.setTrangThai(mContext.getString(R.string.status_hoan_thanh));
                        donHang.setIdKhachHang(dh.getIdKhachHang());
                        donHang.setIdSanPhams(dh.getIdSanPhams());
                        mValues.remove(dh);
                        dbController.capNhatDonHang(donHang);
                        clearSelect();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clearSelect();
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private List<String> getTagSanPhams(List<SanPham> sanPhams) {
        List<String> sanPhamString = new ArrayList<>();
        for (SanPham sanPham : sanPhams) {
            sanPhamString.add(sanPham.getTenSP());
        }
        return sanPhamString;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void thanhToanList() {
        if (itemStateArray.size() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            String formattedPrice = new DecimalFormat("##,##0VNĐ").format(tongTien());
            builder.setTitle("Thanh toán đơn hàng?")
                    .setMessage("Thành tiền " + formattedPrice)
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            for (int i = 0; i < getItemCount(); i++) {
                                if (itemStateArray.get(i, false)) {
                                    DonHang dh = mValues.get(i);
                                    DonHang donHang = new DonHang();
                                    donHang.setId(dh.getId());
                                    donHang.setThoiGianTao(dh.getThoiGianTao());
                                    donHang.setTrangThai(mContext.getString(R.string.status_hoan_thanh));
                                    donHang.setIdKhachHang(dh.getIdKhachHang());
                                    donHang.setIdSanPhams(dh.getIdSanPhams());
                                    mValues.remove(dh);
                                    dbController.capNhatDonHang(donHang);
                                }
                            }
                            clearSelect();
                        }
                    })
                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            clearSelect();
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Utils.showToast(mContext, "Vui lòng chọn đơn hàng cần thanh toán ");
        }
    }

    private long tongTien() {
        long tong = 0;
        for (int i = 0; i < getItemCount(); i++) {
            if (itemStateArray.get(i, false)) {
                DonHang dh = mValues.get(i);
                tong += dh.getTongTien(mContext);
            }
        }
        return tong;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mTvThoiGianTao;
        final ImageView ivIcon;
        final TextView mTvKhachHang;
        final TagCloudView tcvSanPham;
        final TextView mTvTongTien;
        final LinearLayout mSeparateLine;
        final LinearLayout mLayoutChinhSua;
        final ImageButton btnThanhToan;
        final ImageButton btnChinhSua;
        DonHang mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTvThoiGianTao = (TextView) view.findViewById(R.id.tvThoiGianTao);
            ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            mTvKhachHang = (TextView) view.findViewById(R.id.tvKhachHang);
            tcvSanPham = (TagCloudView) view.findViewById(R.id.tcvSanPham);
            mTvTongTien = (TextView) view.findViewById(R.id.tvTongTien);
            mSeparateLine = (LinearLayout) view.findViewById(R.id.separateLine);
            mLayoutChinhSua = (LinearLayout) view.findViewById(R.id.layoutChinhSua);
            btnThanhToan = (ImageButton) view.findViewById(R.id.btnThanhToan);
            btnChinhSua = (ImageButton) view.findViewById(R.id.btnChinhSua);
        }
    }
}

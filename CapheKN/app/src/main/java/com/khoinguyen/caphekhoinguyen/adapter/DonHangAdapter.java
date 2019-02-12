package com.khoinguyen.caphekhoinguyen.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {

    private List<DonHang> mValues;
    private List<DonHang> mValuesFilter;
    private Context mContext;
    private boolean mIsSua;
    private DBController dbController;

    public DonHangAdapter(Context context, List<DonHang> items) {
        mContext = context;
        mValues = items;
        mIsSua = false;
        dbController = new DBController(context);

        setData();
    }

    public DonHangAdapter(Context context, List<DonHang> items, boolean isSua) {
        mContext = context;
        mValues = items;
        mIsSua = isSua;
        dbController = new DBController(context);

        setData();
    }

    public void setData() {
        mValuesFilter = new ArrayList<>();
        for (DonHang order : mValues) {
            if (TextUtils.equals(order.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly))) {
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValuesFilter.get(position);
        String thoiGian = String.format(Locale.US, "%d.", (position + 1)) + Utils.convTimestamp(holder.mItem.getThoiGianTao());
        holder.mTvThoiGianTao.setText(thoiGian);
        if (holder.mItem.getKhachHang() != null) {
            holder.mTvKhachHang.setText(holder.mItem.getKhachHang().getTenKH());
        } else {
            holder.mTvKhachHang.setText("Khách vãng lai");
        }
        List<SanPham> sanPhams = holder.mItem.getSanPhams();
        if (sanPhams != null) {
            holder.mTvSanPham.setText(getSanPhamList(sanPhams));
            String formattedPrice = new DecimalFormat("##,##0VNĐ").format(getTongTien(sanPhams));
            holder.mTvTongTien.setText(formattedPrice);
        } else {
            holder.mTvSanPham.setText("---");
        }

        boolean isTracking = TextUtils.equals(holder.mItem.getTrangThai(), mContext.getString(R.string.status_dang_xu_ly));
        holder.mIvTrangThai.setImageResource(isTracking ? R.drawable.orderlist_07 : R.drawable.orderlist_08);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    private void chinhSua(final DonHang donHang, View view) {
        PopupMenu popup = new PopupMenu(mContext, view);
        popup.inflate(R.menu.menu_don_hang);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.option_cap_nhat:
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
                        mValues.set(mValues.indexOf(donHang), dh);
                        setData();
                        dbController.capNhatDonHang(dh);
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

    private void capNhat(final DonHang dh) {
        final DonHang donHang = new DonHang();
        donHang.setId(dh.getId());
        donHang.setThoiGianTao(dh.getThoiGianTao());
        donHang.setTrangThai(dh.getTrangThai());
        donHang.setKhachHang(dh.getKhachHang());

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
        builder.setTitle("Bán hàng");
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_them_don_hang, null);

        final List<KhachHang> khachHangs = dbController.layDanhSachKhachHang();
        KhachHangArrayAdapter adapterKH = new KhachHangArrayAdapter(mContext, android.R.layout.simple_dropdown_item_1line, khachHangs);
        AutoCompleteTextView etKhachHang = (AutoCompleteTextView) view.findViewById(R.id.etKhachHang);
        etKhachHang.setThreshold(1);
        etKhachHang.setAdapter(adapterKH);
        if (donHang.getKhachHang() != null)
            etKhachHang.setText(donHang.getKhachHang().getTenKH());
        else
            etKhachHang.setEnabled(false);
        etKhachHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KhachHang khachHang = (KhachHang) parent.getItemAtPosition(position);
                donHang.setKhachHang(khachHang);
            }
        });

        final List<SanPham> sanPhams = dbController.layDanhSachSanPham();
        SanPhamArrayAdapter adapterSP = new SanPhamArrayAdapter(mContext, android.R.layout.simple_dropdown_item_1line, sanPhams);
        final MultiAutoCompleteTextView etSanPham = (MultiAutoCompleteTextView) view.findViewById(R.id.etSanPham);
        etSanPham.setThreshold(1);
        etSanPham.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        etSanPham.setAdapter(adapterSP);
        String sanPhamString = "";
        for (SanPham sanPham : dh.getSanPhams()) {
            sanPhamString += sanPham.getTenSP() + ",";
        }
        etSanPham.setText(sanPhamString);
        etSanPham.requestFocus();

        builder.setView(view);

        builder.setPositiveButton(mIsSua ? "Sửa" : "Thêm", new DialogInterface.OnClickListener() {
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
                if (donHang.getSanPhams() != null) {
                    dbController.capNhatDonHang(donHang);
                    if (mIsSua && donHang.getKhachHang().getId() != dh.getKhachHang().getId()) {
                        mValues.remove(dh);
                    } else
                        mValues.set(mValues.indexOf(dh), donHang);
                    setData();

                    notifyDataSetChanged();
                } else {
                    Utils.showToast(mContext, "Cập nhật đơn hàng thất bại");
                }
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

    private void thanhToan(final DonHang donHang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Thanh toán đơn hàng?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DonHang dh = donHang;
                        donHang.setTrangThai(mContext.getString(R.string.status_hoan_thanh));
                        mValues.set(mValues.indexOf(donHang), dh);
                        setData();
                        dbController.capNhatDonHang(dh);
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

    private long getTongTien(List<SanPham> sanPhams) {
        long tongTien = 0;
        for (SanPham sanPham : sanPhams) {
            tongTien += sanPham.getDonGia();
        }
        return tongTien;
    }

    private String getSanPhamList(List<SanPham> sanPhams) {
        String sanPhamString = sanPhams.get(0).getTenSP();
        for (int i = 1; i < sanPhams.size(); i++) {
            sanPhamString += ", " + sanPhams.get(i).getTenSP();
        }
        return sanPhamString;
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
        final TextView mTvTongTien;
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
            mTvTongTien = (TextView) view.findViewById(R.id.tvTongTien);
            btnThanhToan = (ImageButton) view.findViewById(R.id.btnThanhToan);
            btnChinhSua = (ImageButton) view.findViewById(R.id.btnChinhSua);
        }
    }
}

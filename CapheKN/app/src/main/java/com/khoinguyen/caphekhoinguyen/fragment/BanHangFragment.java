package com.khoinguyen.caphekhoinguyen.fragment;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.adapter.DonHangAdapter;
import com.khoinguyen.caphekhoinguyen.adapter.KhachHangArrayAdapter;
import com.khoinguyen.caphekhoinguyen.adapter.SanPhamArrayAdapter;
import com.khoinguyen.caphekhoinguyen.adapter.SimpleSectionedAdapter;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.event.DonHangEvent;
import com.khoinguyen.caphekhoinguyen.model.DonHang;
import com.khoinguyen.caphekhoinguyen.model.KhachHang;
import com.khoinguyen.caphekhoinguyen.model.SanPham;
import com.khoinguyen.caphekhoinguyen.realtimedatabase.RealtimeDatabaseController;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;
import com.khoinguyen.caphekhoinguyen.utils.Utils;
import com.melnykov.fab.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BanHangFragment extends Fragment {
    private static final String TAG = "BanHangFragment";

    private OnBanHangInteractionListener mListener;

    private List<DonHang> mDonHangs;
    private RecyclerView mRecyclerView;
    private DonHangAdapter mAdapter;
    private SimpleSectionedAdapter mSectionedAdapter;
    private View layoutTotal;
    private View layoutMoney;
    private ImageButton btnGoUpDown;
    private TextView tvTotalCost;
    private Animation animGoUp;
    private Animation animGoDown;
    private MenuItem actionThanhToan;
    private AlertDialog alertDialog;
    private Calendar mCalendar;

    public BanHangFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ban_hang, container, false);
        mRecyclerView = view.findViewById(R.id.rvBanHang);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> banHang());
        fab.attachToRecyclerView(mRecyclerView);
        layoutTotal = view.findViewById(R.id.layoutTotal);
        btnGoUpDown = view.findViewById(R.id.btnGoUpDown);
        layoutMoney = view.findViewById(R.id.layoutMoney);
        tvTotalCost = view.findViewById(R.id.tvTotalCost);
        btnGoUpDown.setImageResource(layoutMoney.getVisibility() == View.VISIBLE ? R.drawable.orderlist_02 : R.drawable.orderlist_01);
        btnGoUpDown.setOnClickListener(v -> {
            if (layoutMoney.getVisibility() == View.GONE) {
                btnGoUpDown.setImageResource(R.drawable.orderlist_02);
                layoutTotal.startAnimation(animGoUp);
            } else {
                btnGoUpDown.setImageResource(R.drawable.orderlist_01);
                layoutTotal.startAnimation(animGoDown);
            }
        });

        animGoUp = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);
        animGoUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                layoutMoney.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animGoDown = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        animGoDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layoutMoney.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        actionThanhToan = menu.findItem(R.id.action_thanh_toan);
        actionThanhToan.setVisible(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_ban_hang, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_thanh_toan:
                thanhToan();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void thanhToan() {
        mAdapter.thanhToanList();
    }

    @SuppressLint("DefaultLocale")
    private void banHang() {
        mAdapter.clearSelect();

        final DonHang donHang = new DonHang();
        mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(System.currentTimeMillis());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Bán hàng");
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_them_don_hang, null);

        final List<KhachHang> khachHangs = DBController.getInstance(getActivity()).layDanhSachKhachHang();
        KhachHangArrayAdapter adapterKH = new KhachHangArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, khachHangs);
        AutoCompleteTextView etKhachHang = view.findViewById(R.id.etKhachHang);
        etKhachHang.setThreshold(1);
        etKhachHang.setAdapter(adapterKH);
        etKhachHang.setOnItemClickListener((parent, v, position, id) -> {
            KhachHang khachHang = (KhachHang) parent.getItemAtPosition(position);
            donHang.setIdKhachHang(khachHang.getId());
        });

        final List<SanPham> sanPhams = DBController.getInstance(getActivity()).layDanhSachSanPham();
        SanPhamArrayAdapter adapterSP = new SanPhamArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, sanPhams);
        final MultiAutoCompleteTextView etSanPham = view.findViewById(R.id.etSanPham);
        etSanPham.setThreshold(1);
        etSanPham.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        etSanPham.setAdapter(adapterSP);

        final Button btnThemKhachHang = view.findViewById(R.id.btnThemKhachHang);
        btnThemKhachHang.setOnClickListener(v -> {
            if (mListener != null)
                mListener.onThemKhachHangClick();
            alertDialog.cancel();
        });

        final Button btnThemSanPham = view.findViewById(R.id.btnThemSanPham);
        btnThemSanPham.setOnClickListener(v -> {
            if (mListener != null)
                mListener.onThemSanPhamClick();
            alertDialog.cancel();
        });

        final TextView tvNgay = view.findViewById(R.id.tvNgay);
        final TextView tvThoiGian = view.findViewById(R.id.tvThoiGian);
        tvThoiGian.setText(String.format("%1$tH:%1$tM", mCalendar));
        tvNgay.setText(String.format("%1$td/%1$tm/%1$ty", mCalendar));

        tvNgay.setOnClickListener(v -> showDatePickerDialog(tvNgay));

        tvThoiGian.setOnClickListener(v -> showTimePickerDialog(tvThoiGian));

        builder.setView(view);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            donHang.setId(RealtimeDatabaseController.getInstance(getActivity()).genKeyDonHang());
            donHang.setThoiGianTao(mCalendar.getTimeInMillis());
            donHang.setTrangThai(getString(R.string.status_dang_xu_ly));
            String[] tenSPs = etSanPham.getText().toString().split(",");
            for (String tenSP : tenSPs) {
                for (SanPham sanPham : sanPhams) {
                    if (TextUtils.equals(tenSP.trim(), sanPham.getTenSP())) {
                        donHang.addSanPham(sanPham);
                        break;
                    }
                }
            }
            if (donHang.getIdSanPhams() == null) {
                Utils.showToast(getActivity(), "Thêm đơn hàng thất bại");
            } else {
                DBController.getInstance(getActivity()).themDonHang(donHang);
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void showTimePickerDialog(final TextView tvThoiGian) {
        new TimePickerDialog(getActivity(), (timePicker, hour, minute) -> {
            mCalendar.set(Calendar.HOUR, hour);
            mCalendar.set(Calendar.MINUTE, minute);
            mCalendar.clear(Calendar.SECOND);
            mCalendar.clear(Calendar.MILLISECOND);
            tvThoiGian.setText(String.format("%1$tH:%1$tM", mCalendar));
        }, mCalendar.get(Calendar.HOUR), mCalendar.get(Calendar.MINUTE), true).show();
    }

    @SuppressLint("DefaultLocale")
    private void showDatePickerDialog(final TextView tvNgay) {
        new DatePickerDialog(getActivity(), (datePicker, year, month, dayOfMonth) -> {
            mCalendar.set(year, month, dayOfMonth);
            tvNgay.setText(String.format("%1$td/%1$tm/%1$ty", mCalendar));
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (null != mSectionedAdapter) {
            mRecyclerView.setAdapter(mSectionedAdapter);
        } else {
            getDonHangs();
        }
    }

    private void getDonHangs() {
        showLoading();
        if (actionThanhToan != null)
            actionThanhToan.setVisible(false);
        new MyTask().execute();
    }

    private void showLoading() {
//        Utils.showProgressDialog(getActivity());
    }

    private void hideLoading() {
//        Utils.hideProgressDialog();
    }

    private List<SimpleSectionedAdapter.Section> getSections() {
        if (mDonHangs.isEmpty()) return new ArrayList<>();
        List<SimpleSectionedAdapter.Section> sections = new ArrayList<>();
        sections.add(new SimpleSectionedAdapter.Section(0, mDonHangs.get(0).getThoiGianTao()));
        for (int i = 1; i < mDonHangs.size(); i++) {
            if (!TextUtils.equals(Utils.convTimestamp(mDonHangs.get(i).getThoiGianTao(), "dd/MM/yyyy"), Utils.convTimestamp(mDonHangs.get(i - 1).getThoiGianTao(), "dd/MM/yyyy")))
                sections.add(new SimpleSectionedAdapter.Section(i, mDonHangs.get(i).getThoiGianTao()));
        }
        return sections;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DonHangEvent event) {
        LogUtils.d(TAG, "onMessageEvent: " + event.getDonHang().getId());
        getDonHangs();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TrangChuFragment.OnTrangChuInteractionListener) {
            mListener = (OnBanHangInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnOrderInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnBanHangInteractionListener {
        void onThemKhachHangClick();

        void onThemSanPhamClick();
    }

    public interface OnDonHangListerner {
        void onShow();

        void onHide();

        void onUpdateTongTien(long tongTien);
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mDonHangs = DBController.getInstance(getActivity()).layDonHangDangXuLy();
            mAdapter = new DonHangAdapter(getActivity(), mDonHangs, new OnDonHangListerner() {
                @Override
                public void onShow() {
                    actionThanhToan.setVisible(true);
                }

                @Override
                public void onHide() {
                    actionThanhToan.setVisible(false);
                }

                @Override
                public void onUpdateTongTien(long tongTien) {
                    String formattedPrice = new DecimalFormat("##,##0VNĐ").format(tongTien);
                    tvTotalCost.setText(formattedPrice);
                }
            }, mListener);
            mSectionedAdapter = new SimpleSectionedAdapter(getActivity(), getSections(), mAdapter);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRecyclerView.setAdapter(mSectionedAdapter);
            hideLoading();
        }
    }
}

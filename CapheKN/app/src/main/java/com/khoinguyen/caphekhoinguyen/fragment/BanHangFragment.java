package com.khoinguyen.caphekhoinguyen.fragment;


import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
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
    private MenuItem actionChinhSua;
    private DBController dbController;
    private AlertDialog alertDialog;

    public BanHangFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ban_hang, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvBanHang);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banHang();
            }
        });
        fab.attachToRecyclerView(mRecyclerView);
        layoutTotal = view.findViewById(R.id.layoutTotal);
        btnGoUpDown = (ImageButton) view.findViewById(R.id.btnGoUpDown);
        layoutMoney = view.findViewById(R.id.layoutMoney);
        tvTotalCost = (TextView) view.findViewById(R.id.tvTotalCost);
        btnGoUpDown.setImageResource(layoutMoney.getVisibility() == View.VISIBLE ? R.drawable.orderlist_02 : R.drawable.orderlist_01);
        btnGoUpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutMoney.getVisibility() == View.GONE) {
                    btnGoUpDown.setImageResource(R.drawable.orderlist_02);
                    layoutTotal.startAnimation(animGoUp);
                } else {
                    btnGoUpDown.setImageResource(R.drawable.orderlist_01);
                    layoutTotal.startAnimation(animGoDown);
                }
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

        dbController = DBController.getInstance(getActivity());
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
        actionChinhSua = menu.findItem(R.id.action_thanh_toan);
        actionChinhSua.setVisible(false);
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

    private void banHang() {
        mAdapter.clearSelect();

        final DonHang donHang = new DonHang();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Bán hàng");
        builder.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_them_don_hang, null);

        final List<KhachHang> khachHangs = dbController.layDanhSachKhachHang();
        KhachHangArrayAdapter adapterKH = new KhachHangArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, khachHangs);
        AutoCompleteTextView etKhachHang = (AutoCompleteTextView) view.findViewById(R.id.etKhachHang);
        etKhachHang.setThreshold(1);
        etKhachHang.setAdapter(adapterKH);
        etKhachHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KhachHang khachHang = (KhachHang) parent.getItemAtPosition(position);
                donHang.setIdKhachHang(khachHang.getId());
            }
        });

        final List<SanPham> sanPhams = dbController.layDanhSachSanPham();
        SanPhamArrayAdapter adapterSP = new SanPhamArrayAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, sanPhams);
        final MultiAutoCompleteTextView etSanPham = (MultiAutoCompleteTextView) view.findViewById(R.id.etSanPham);
        etSanPham.setThreshold(1);
        etSanPham.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        etSanPham.setAdapter(adapterSP);

        final Button btnThemKhachHang = (Button) view.findViewById(R.id.btnThemKhachHang);
        btnThemKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onThemKhachHangClick();
                alertDialog.cancel();
            }
        });

        final Button btnThemSanPham = (Button) view.findViewById(R.id.btnThemSanPham);
        btnThemSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null)
                    mListener.onThemSanPhamClick();
                alertDialog.cancel();
            }
        });

        builder.setView(view);

        builder.setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                donHang.setId(RealtimeDatabaseController.getInstance().genKeyDonHang());
                donHang.setThoiGianTao(System.currentTimeMillis());
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
                if (donHang.getIdSanPhams() != null) {
                    dbController.themDonHang(donHang);
                    mDonHangs.add(0, donHang);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Utils.showToast(getActivity(), "Thêm đơn hàng thất bại");
                }
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
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
        mDonHangs = dbController.layDonHangDangXuLy();
        mAdapter = new DonHangAdapter(getActivity(), mDonHangs, new OnDonHangListerner() {
            @Override
            public void onShow() {
                actionChinhSua.setVisible(true);
            }

            @Override
            public void onHide() {
                actionChinhSua.setVisible(false);
            }

            @Override
            public void onRefresh() {
                getDonHangs();
            }

            @Override
            public void onUpdateTongTien(long tongTien) {
                String formattedPrice = new DecimalFormat("##,##0VNĐ").format(tongTien);
                tvTotalCost.setText(formattedPrice);
            }
        }, mListener);
        mSectionedAdapter = new SimpleSectionedAdapter(getActivity(), getSections(), mAdapter);
        mRecyclerView.setAdapter(mSectionedAdapter);
    }

    private List<SimpleSectionedAdapter.Section> getSections() {
        if (mDonHangs.size() == 0) return new ArrayList<>();
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

        void onRefresh();

        void onUpdateTongTien(long tongTien);
    }
}

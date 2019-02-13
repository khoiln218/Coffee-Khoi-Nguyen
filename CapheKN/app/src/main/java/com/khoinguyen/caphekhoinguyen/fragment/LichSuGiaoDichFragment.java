package com.khoinguyen.caphekhoinguyen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.R;
import com.khoinguyen.caphekhoinguyen.adapter.DonHangAdapter;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.model.DonHang;

import java.text.DecimalFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class LichSuGiaoDichFragment extends Fragment {
    private List<DonHang> mDonHangs;
    private RecyclerView mRecyclerView;
    private DonHangAdapter mAdapter;
    private View layoutTotal;
    private View layoutMoney;
    private ImageButton btnGoUpDown;
    private TextView tvTotalCost;
    private Animation animGoUp;
    private Animation animGoDown;
    private MenuItem actionChinhSua;
    private DBController dbController;
    private int idKhachHang;

    public LichSuGiaoDichFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su_giao_dich, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvLichSuGiaoDich);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
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

        idKhachHang = getArguments().getInt("idKhachHang");

        dbController = new DBController(getActivity());
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
        inflater.inflate(R.menu.menu_ban_hang, menu);
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

    @Override
    public void onResume() {
        super.onResume();
        if (null != mAdapter) {
            mRecyclerView.setAdapter(mAdapter);
        } else {
            getDonHangs();
        }
    }

    private void getDonHangs() {
        mDonHangs = dbController.layDonHangDangXuLyTheoKhachHang(idKhachHang);
        mAdapter = new DonHangAdapter(getActivity(), mDonHangs, new BanHangFragment.OnDonHangListerner() {
            @Override
            public void onShow() {
                actionChinhSua.setVisible(true);
            }

            @Override
            public void onHide() {
                actionChinhSua.setVisible(false);
            }

            @Override
            public void onUpdateTongTien(long tongTien) {
                String formattedPrice = new DecimalFormat("##,##0VNĐ").format(tongTien);
                tvTotalCost.setText(formattedPrice);
            }
        }, true);
        mRecyclerView.setAdapter(mAdapter);
    }
}

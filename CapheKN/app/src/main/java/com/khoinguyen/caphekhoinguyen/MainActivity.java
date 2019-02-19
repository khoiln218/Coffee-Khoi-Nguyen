package com.khoinguyen.caphekhoinguyen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.khoinguyen.caphekhoinguyen.controller.DBController;
import com.khoinguyen.caphekhoinguyen.event.NetStatusEvent;
import com.khoinguyen.caphekhoinguyen.fragment.BanHangFragment;
import com.khoinguyen.caphekhoinguyen.fragment.BaoCaoFragment;
import com.khoinguyen.caphekhoinguyen.fragment.CongNoFragment;
import com.khoinguyen.caphekhoinguyen.fragment.KhachHangFragment;
import com.khoinguyen.caphekhoinguyen.fragment.LichSuGiaoDichFragment;
import com.khoinguyen.caphekhoinguyen.fragment.PhuHoFragment;
import com.khoinguyen.caphekhoinguyen.fragment.SanPhamFragment;
import com.khoinguyen.caphekhoinguyen.fragment.TrangChuFragment;
import com.khoinguyen.caphekhoinguyen.realtimedatabase.RealtimeDatabaseController;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        TrangChuFragment.OnTrangChuInteractionListener, KhachHangFragment.OnKhachHangInteractionListener, BanHangFragment.OnBanHangInteractionListener {
    private static final String TAG = "MainActivity";

    private TextView toolbarTitle;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private LinearLayout mLayoutMang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mToggle.setDrawerIndicatorEnabled(true);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mToggle.isDrawerIndicatorEnabled()) {
                    onBackPressed();
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
            }
        });

        mLayoutMang = findViewById(R.id.layoutMang);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Fragment fr = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fr != null) {
                    int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
                    LogUtils.d(TAG, "backStackEntryCount: " + backStackEntryCount);
                    toolbarTitle.setText(fr.getTag());
                    setNavIcon();
                }
            }
        });

        openHome();
    }

    protected void setNavIcon() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        setDrawerEnabled(backStackEntryCount == 0);
    }

    protected void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        mDrawer.setDrawerLockMode(lockMode);
        mToggle.setDrawerIndicatorEnabled(enabled);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_dong_bo:
                dongBo();
                break;
            case R.id.nav_tai_du_lieu_len:
                taiDuLieuLenServer();
                break;
            case R.id.nav_cai_dat:
                caiDat();
                break;
            case R.id.nav_log_out:
                logOut();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void taiDuLieuLenServer() {
        DBController.getInstance(this).taiDuLieuLenServer();
    }

    private void dongBo() {
        RealtimeDatabaseController.getInstance().dongBo(this);
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, SignInActivity.class));
        finish();
    }

    private void caiDat() {
    }

    private void openHome() {
        TrangChuFragment fragment = new TrangChuFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, getString(R.string.title_trang_chu))
                .commit();
        toolbarTitle.setText(R.string.title_trang_chu);
    }

    private void openBaoCao() {
        BaoCaoFragment fragment = new BaoCaoFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, getString(R.string.title_bao_cao))
                .addToBackStack(null)
                .commit();
        toolbarTitle.setText(R.string.title_bao_cao);
    }

    private void openKhachHang() {
        KhachHangFragment fragment = new KhachHangFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, getString(R.string.title_khach_hang))
                .addToBackStack(null)
                .commit();
        toolbarTitle.setText(R.string.title_khach_hang);
    }

    private void openCongNo() {
        CongNoFragment fragment = new CongNoFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, getString(R.string.title_top_cong_no))
                .addToBackStack(null)
                .commit();
        toolbarTitle.setText(R.string.title_top_cong_no);
    }

    private void openPhuHo() {
        PhuHoFragment fragment = new PhuHoFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, getString(R.string.title_top_phu_ho))
                .addToBackStack(null)
                .commit();
        toolbarTitle.setText(R.string.title_top_phu_ho);
    }

    private void openSanPham() {
        SanPhamFragment fragment = new SanPhamFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, getString(R.string.title_sp_dv))
                .addToBackStack(null)
                .commit();
        toolbarTitle.setText(R.string.title_sp_dv);
    }

    private void openBanHang() {
        BanHangFragment fragment = new BanHangFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, getString(R.string.title_ban_hang))
                .addToBackStack(null)
                .commit();
        toolbarTitle.setText(R.string.title_ban_hang);
    }

    @Override
    public void onBanHangClick() {
        openBanHang();
    }

    @Override
    public void onSanPhamClick() {
        openSanPham();
    }

    @Override
    public void onBaoCaoClick() {
        openBaoCao();
    }

    @Override
    public void onCongNoClick() {
        openCongNo();
    }

    @Override
    public void onPhuHoClick() {
        openPhuHo();
    }

    @Override
    public void onKhachHangClick() {
        openKhachHang();
    }

    @Override
    public void onKhachHangInteraction(String idKhachHang, int trangThai) {
        LichSuGiaoDichFragment fragment = new LichSuGiaoDichFragment();
        Bundle bundle = new Bundle();
        bundle.putString("idKhachHang", idKhachHang);
        bundle.putInt("trangThai", trangThai);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment, getString(R.string.title_lich_su_giao_dich))
                .addToBackStack(null)
                .commit();
        toolbarTitle.setText(R.string.title_lich_su_giao_dich);
    }

    @Override
    public void onThemKhachHangClick() {
        openKhachHang();
    }

    @Override
    public void onThemSanPhamClick() {
        openSanPham();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetStatusEvent event) {
        LogUtils.d(TAG, "onMessageEvent: " + event.isConnect());
        mLayoutMang.setVisibility(event.isConnect() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RealtimeDatabaseController.getInstance().startListerner(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RealtimeDatabaseController.getInstance().stopListerner();
        EventBus.getDefault().unregister(this);
    }
}

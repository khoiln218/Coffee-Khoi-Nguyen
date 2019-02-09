package com.khoinguyen.caphekhoinguyen;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.khoinguyen.caphekhoinguyen.fragment.BanHangFragment;
import com.khoinguyen.caphekhoinguyen.fragment.BaoCaoFragment;
import com.khoinguyen.caphekhoinguyen.fragment.CongNoFragment;
import com.khoinguyen.caphekhoinguyen.fragment.KhachHangFragment;
import com.khoinguyen.caphekhoinguyen.fragment.PhuHoFragment;
import com.khoinguyen.caphekhoinguyen.fragment.SanPhamFragment;
import com.khoinguyen.caphekhoinguyen.fragment.TrangChuFragment;
import com.khoinguyen.caphekhoinguyen.utils.LogUtils;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TrangChuFragment.OnTrangChuInteractionListener {

    private static final String TAG = "MainActivity";
    private TextView toolbarTitle;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        mToggle.setDrawerIndicatorEnabled(true);
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_ban_hang:
                openBanHang();
                break;
            case R.id.nav_sp_dv:
                openSanPham();
                break;
            case R.id.nav_top_phu_ho:
                openPhuHo();
                break;
            case R.id.nav_top_cong_no:
                openCongNo();
                break;
            case R.id.nav_khach_hang:
                openKhachHang();
                break;
            case R.id.nav_bao_cao:
                openBaoCao();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}

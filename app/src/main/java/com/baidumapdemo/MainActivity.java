package com.baidumapdemo;


import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.baidumapdemo.fragment.LocationFragment;
import com.baidumapdemo.fragment.MarkFragment;
import com.baidumapdemo.fragment.NavigationFragment;
import com.baidumapdemo.fragment.SearchFragment;
import com.baidumapdemo.permission.PermissionTool;
import com.baidumapdemo.permission.RuntimeRationale;
import com.baidumapdemo.util.BottomNavigationViewHelper;
import com.baidumapdemo.util.ToastUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    private List<Fragment> fragmentList;
    private int lastIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        // 将item 设置为不移动
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initFragment();
        getLocationPermission();
        selectFragment(0);
    }

    /**
     * 设置默认选中fragment
     * @param index 碎片fragment
     */
    private void selectFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = fragmentList.get(index);
        Fragment lastFragment = fragmentList.get(lastIndex);
        lastIndex = index;
        ft.hide(lastFragment);
        if (!currentFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            ft.add(R.id.frame_layout, currentFragment);
        }
        ft.show(currentFragment);
        ft.commitAllowingStateLoss();
    }


    /**
     * 初始化碎片
     */
    private void initFragment() {
        fragmentList = new ArrayList<>();
        fragmentList.add(LocationFragment.getInstance());//定位当前位置
        fragmentList.add(NavigationFragment.getInstance());//导航
        fragmentList.add(SearchFragment.getInstance());//轨迹
        fragmentList.add(MarkFragment.getInstance());//Mark标注
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_location:
                    selectFragment(0);
                    item.setIcon(R.drawable.ic_location_select);
                    return true;
                case R.id.navigation_navigation:
                    selectFragment(1);
                    item.setIcon(R.drawable.ic_nav_select);
                    return true;
                case R.id.navigation_route:
                    selectFragment(2);
                    item.setIcon(R.drawable.ic_search_select);
                    return true;
                case R.id.navigation_mark:
                    selectFragment(3);
                    item.setIcon(R.drawable.ic_mark_select);
                    return true;
                default:
                    break;
            }
            return false;
        }
    };

    private void getLocationPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE, Permission.Group.LOCATION)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        ToastUtil.show(MainActivity.this, "定位权限被拒绝，请手动开启！");
                        if (AndPermission.hasAlwaysDeniedPermission(MainActivity.this, permissions)) {
                            PermissionTool.showSettingDialog(MainActivity.this, permissions);
                        }
                    }
                })
                .start();
    }

}

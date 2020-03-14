package com.baidumapdemo.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidumapdemo.R;
import com.baidumapdemo.service.BaiDuService;
import com.baidumapdemo.ui.DriveActivity;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationFragment extends Fragment {

    @BindView(R.id.tv_car)
    TextView tv_car;
    @BindView(R.id.tv_walk)
    TextView tv_walk;
    @BindView(R.id.tv_riding)
    TextView tv_riding;

    private View mView;

    public static NavigationFragment getInstance() {
        return new NavigationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_navigation, container,
                false);
        ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    private void initView(){
        // 开启前台服务防止应用进入后台gps挂掉
        getActivity().startService(new Intent(getActivity(), BaiDuService.class));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            }
            if (getActivity().checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }

            if (permissions.size() != 0) {
                requestPermissionsForM(permissions);
            }
        }

    }

    private void requestPermissionsForM(final ArrayList<String> per) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(per.toArray(new String[per.size()]), 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity(), BaiDuService.class));
    }

    @OnClick({R.id.tv_car,R.id.tv_walk,R.id.tv_riding})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_car:
                DriveActivity.startActivity(getActivity());
                break;
            case R.id.tv_walk:

                break;
            case R.id.tv_riding:
                break;
        }
    }
}

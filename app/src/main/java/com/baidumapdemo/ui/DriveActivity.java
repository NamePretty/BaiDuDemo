package com.baidumapdemo.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidumapdemo.R;
import com.baidumapdemo.permission.PermissionTool;
import com.baidumapdemo.permission.RuntimeRationale;
import com.baidumapdemo.util.ToastUtil;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DriveActivity extends AppCompatActivity {


    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive);
        ButterKnife.bind(this);
        initView();
        getLocationPermission();
    }

    private void initView(){

    }

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
                        ToastUtil.show(DriveActivity.this, "定位权限被拒绝，请手动开启！");
                        if (AndPermission.hasAlwaysDeniedPermission(DriveActivity.this, permissions)) {
                            PermissionTool.showSettingDialog(DriveActivity.this, permissions);
                        }
                    }
                })
                .start();
    }



    public static void startActivity(Context context) {
        Intent intent = new Intent(context, DriveActivity.class);
        context.startActivity(intent);
    }

}

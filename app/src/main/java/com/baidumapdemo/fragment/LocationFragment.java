package com.baidumapdemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidumapdemo.R;
import com.baidumapdemo.dialog.GpsRemindDialog;
import com.baidumapdemo.permission.PermissionTool;
import com.baidumapdemo.permission.RuntimeRationale;
import com.baidumapdemo.util.BaiduLocationUtils;
import com.baidumapdemo.util.ToastUtil;
import com.baidumapdemo.util.Utils;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationFragment extends Fragment {


    @BindView(R.id.baidu_map_view)
    TextureMapView baidu_map_view;
    @BindView(R.id.tv_adress)
    TextView tv_adress;
    @BindView(R.id.tv_location)
    TextView tv_location;
    private View mView;
    private BaiduMap mBaiduMap;
    private String lng_lat;
    private LatLng c_pt;
    private BaiduLocationUtils baiduLocationUtils;
    public static LocationFragment getInstance() {
        return new LocationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_location, container,
                false);
        ButterKnife.bind(this, mView);
        initLocation();
        initView();
        return mView;
    }

    private void initView(){
        mBaiduMap = baidu_map_view.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }

    private void initLocation(){
        baiduLocationUtils = new BaiduLocationUtils(getActivity(), new BaiduLocationUtils.OnLocateCompletedListener() {

            @Override
            public void onLocateCompleted(double latitude, double longitude, BDLocation location) {
                setLocationData(latitude, longitude, location);
            }
        });
    }


    public void setLocationData(double latitude, double longitude, BDLocation location) {
        if(location!=null) {
            String mLocationStr = location.getAddrStr();
            tv_adress.setText("当前位置："+mLocationStr);
            lng_lat = longitude + "," + latitude;
            initBaiduMapView();
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
        }
    }

    private void initBaiduMapView() {
        if (TextUtils.isEmpty(lng_lat)) {
            return;
        }
        String[] arr = lng_lat.split(",");
        if (arr.length != 2) {
            return;
        }
        String lng = arr[0];
        String lat = arr[1];
        c_pt = new LatLng(Utils.toDouble(lat), Utils.toDouble(lng));
        MapStatus mMapStatus = new MapStatus.Builder()//定义地图状态
                .target(c_pt)
                .zoom(18)
                .build();  //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);//改变地图状态
    }

    private void getLocationPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Permission.Group.STORAGE, Permission.Group.LOCATION)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        openGPSSetting();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        ToastUtil.show(getActivity(), "定位权限被拒绝，请手动开启！");
                        if (AndPermission.hasAlwaysDeniedPermission(getActivity(), permissions)) {
                            PermissionTool.showSettingDialog(getActivity(), permissions);
                        }
                    }
                })
                .start();
    }

    private boolean checkGpsIsOpen() {
        boolean isOpen;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        isOpen = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isOpen;
    }

    private void openGPSSetting() {
        if (checkGpsIsOpen()){
            initLocation();
        }else {
            final GpsRemindDialog dialog = new GpsRemindDialog(getActivity());
            dialog.setCancelable(false);
            dialog.show(new GpsRemindDialog.OnChooseClickListener() {
                @Override
                public void onCancel() {
                    dialog.dismiss();
                    getActivity().finish();
                }

                @Override
                public void onOpen() {
                    dialog.dismiss();
                    //跳转到手机原生设置页面
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 11);
                }
            });
        }
    }

    @OnClick({R.id.tv_location})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_location:
                getLocationPermission();
                break;
        }
    }
}

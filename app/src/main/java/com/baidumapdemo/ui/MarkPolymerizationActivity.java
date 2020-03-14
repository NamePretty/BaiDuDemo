package com.baidumapdemo.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidumapdemo.R;
import com.baidumapdemo.model.MarkInfo;
import com.baidumapdemo.util.ToastUtil;
import com.baidumapdemo.util.baidu.Cluster;
import com.baidumapdemo.util.baidu.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkPolymerizationActivity extends AppCompatActivity implements BaiduMap.OnMapLoadedCallback{
    @BindView(R.id.baidu_map_view)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private MapStatus ms;
    private ClusterManager<MarkInfo> mClusterManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_polymerization);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        ms = new MapStatus.Builder().target(new LatLng(31.828541,117.229704)).zoom(8).build();
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        // 定义点聚合管理类ClusterManager
        mClusterManager = new ClusterManager<MarkInfo>(this, mBaiduMap);
        // 添加Marker点
        addMarkers();
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        mBaiduMap.setOnMapStatusChangeListener(mClusterManager);
        // 设置maker点击时的响应
        mBaiduMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MarkInfo>() {
            @Override
            public boolean onClusterClick(Cluster<MarkInfo> cluster) {
                return false;
            }
        });
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkInfo>() {
            @Override
            public boolean onClusterItemClick(MarkInfo item) {
                ToastUtil.show(MarkPolymerizationActivity.this,item.name);
                LatLng movePoint = new LatLng(item.mPosition.latitude,item.mPosition.longitude);
                mBaiduMap.animateMapStatus(
                        MapStatusUpdateFactory.newLatLngZoom(movePoint, 20));
                return false;
            }
        });
    }

    /**
     * 向地图添加Marker点
     */
    public void addMarkers() {
        // 添加Marker点
        LatLng llA = new LatLng(31.896747, 117.378032);
        LatLng llB = new LatLng(31.767654, 117.192334);
        LatLng llC = new LatLng(31.776986, 117.411952);
        LatLng llD = new LatLng(31.868784, 117.29352);
        LatLng llE = new LatLng(31.843512, 117.277997);
        LatLng llF = new LatLng(31.840444, 117.293951);
        LatLng llG = new LatLng(31.862528, 117.309042);

        List<MarkInfo> items = new ArrayList<MarkInfo>();
        items.add(new MarkInfo("01","mark01","1",llA));
        items.add(new MarkInfo("01","mark01","2",llB));
        items.add(new MarkInfo("01","mark01","3",llC));
        items.add(new MarkInfo("01","mark01","4",llD));
        items.add(new MarkInfo("01","mark01","2",llE));
        items.add(new MarkInfo("01","mark01","4",llF));
        items.add(new MarkInfo("01","mark01","1",llG));
        mClusterManager.addItems(items);

    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MarkPolymerizationActivity.class);
        context.startActivity(intent);
    }


}

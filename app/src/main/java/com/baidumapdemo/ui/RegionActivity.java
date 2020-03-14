package com.baidumapdemo.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidumapdemo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegionActivity extends AppCompatActivity implements BaiduMap.OnMapLoadedCallback{
    @BindView(R.id.baidu_map_view)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private MapStatus ms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        ms = new MapStatus.Builder().target(new LatLng(31.785968, 117.343623)).zoom(8).build();
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(this);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        DistrictSearch mDistrictSearch = DistrictSearch.newInstance();//初始化行政区检索
        mDistrictSearch.setOnDistrictSearchListener(listener );//设置回调监听

        DistrictSearchOption districtSearchOption = new DistrictSearchOption();
        districtSearchOption.cityName("合肥");//检索城市名称
        districtSearchOption.districtName("瑶海区");//检索的区县名称
        mDistrictSearch.searchDistrict(districtSearchOption);//请求行政区数据
    }

        OnGetDistricSearchResultListener listener = new OnGetDistricSearchResultListener() {

        @Override
        public void onGetDistrictResult(DistrictResult districtResult) {
            districtResult.getCenterPt();//获取行政区中心坐标点
            districtResult.getCityName();//获取行政区域名称
            List<List<LatLng>> polyLines = districtResult.getPolylines();//获取行政区域边界坐标点
            //边界就是坐标点的集合，在地图上画出来就是多边形图层。有的行政区可能有多个区域，所以会有多个点集合。
            if (polyLines == null) {
                return;
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (List<LatLng> polyline : polyLines) {
                OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                        .points(polyline).dottedLine(true).color(getResources().getColor(R.color.red));
                mBaiduMap.addOverlay(ooPolyline11);
                OverlayOptions ooPolygon = new PolygonOptions().points(polyline)
                        .stroke(new Stroke(5, 0xAA00FF88)).fillColor(0xAAFFFF00);
                mBaiduMap.addOverlay(ooPolygon);
                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));
        }

    };

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

    @Override
    public void onMapLoaded() {
        ms = new MapStatus.Builder().zoom(9).build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegionActivity.class);
        context.startActivity(intent);
    }

}

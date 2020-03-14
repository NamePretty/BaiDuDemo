package com.baidumapdemo.util.baidu;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.model.LatLng;

public interface ClusterItem {

    /**
     * The position of this marker. This must always return the same value.
     */
    LatLng getPosition();

    BitmapDescriptor getBitmapDescriptor();
}
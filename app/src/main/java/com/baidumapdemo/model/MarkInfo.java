package com.baidumapdemo.model;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidumapdemo.R;
import com.baidumapdemo.util.baidu.ClusterItem;

public class MarkInfo implements ClusterItem {
    public String id;
    public String name;//简称
    public String mType;//类型
    public LatLng mPosition;//经纬度

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public MarkInfo( String id, String name, String mType,LatLng latLng){
        this.id=id;
        this.name=name;
        this.mType=mType;
        this.mPosition=latLng;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        if(mType.equals("1")){
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_marka);
        }else if(mType.equals("2")){
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_markb);
        }else if(mType.equals("3")){
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_markc);
        }else if(mType.equals("4")){
            return BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_markd);
        }
        return BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
    }
}

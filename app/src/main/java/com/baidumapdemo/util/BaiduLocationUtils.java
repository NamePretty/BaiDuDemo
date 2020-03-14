package com.baidumapdemo.util;

import android.app.Activity;
import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * author: zdj
 * created on: 2019/07/11 15:22
 * description:
 */
public class BaiduLocationUtils {

    public interface OnLocateCompletedListener {
        /**
         * 除了经纬度以外，如果还需要其他定位信息，可以从location参数获取
         */
        void onLocateCompleted(double latitude, double longitude, BDLocation location);
    }

    private OnLocateCompletedListener mOnLocateCompletedListener;
    private LocationClient mBDLocationClient;
    private BDLocationListener mBDLocationListener;
    //百度地图定位
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private String tempcoor = "bd09ll";
    private Context mContext;

    public BaiduLocationUtils(Context context, OnLocateCompletedListener locateListener) {
        mContext = context;
        mOnLocateCompletedListener = locateListener;
        initLocation();
        startLocation();
    }

    private void initLocation() {
        if (mBDLocationClient == null) {
            mBDLocationClient = new LocationClient(mContext.getApplicationContext());
        }
        if (mBDLocationClient.isStarted()) {
            return;
        }
        mBDLocationListener = new BDLocationListener();
        mBDLocationClient.registerLocationListener(mBDLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//设置定位模式
        option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
        option.setOpenGps(true);
        int span = 0;
        option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
        option.setIsNeedAddress(true);
        mBDLocationClient.setLocOption(option);
    }

    /**
     * 开始定位
     */
    public void startLocation() {
        if (mBDLocationClient != null) {
            if (!mBDLocationClient.isStarted()) {
                mBDLocationClient.start();
            }
        }
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (mBDLocationClient != null) {
            mBDLocationClient.stop();
        }
    }

    public void locate(Activity activity, OnLocateCompletedListener locateListener) {
        new BaiduLocationUtils(activity, locateListener);
    }

    public class BDLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlongitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            LogUtil.e("location.getCity()=" + location.getCity());
            LogUtil.e("BaiduLocationApiDem", sb.toString());
            if (location.getLocType() == 161 || location.getLocType() == 66 || location.getLocType() == 61) {
                SpUtils.put(mContext.getApplicationContext(), "LOCATION_LATITUDE", location.getLatitude());
                SpUtils.put(mContext.getApplicationContext(), "LOCATION_LONGITUDE", location.getLongitude());
                SpUtils.put(mContext.getApplicationContext(), "LOCATION_CITY", location.getCity()+"");
                SpUtils.put(mContext.getApplicationContext(), "LOCATION_ADDRESS", location.getAddrStr());
                Global.LOCATION_SUCCESS_TAG = true;//网络定位成功标识
            } else {
                SpUtils.put(mContext.getApplicationContext(), "LOCATION_LATITUDE", Constants.DEFAULT_LATITUDE);
                SpUtils.put(mContext.getApplicationContext(), "LOCATION_LONGITUDE", Constants.DEFAULT_LONGITUDE);
                SpUtils.put(mContext.getApplicationContext(), "LOCATION_CITY", Constants.DEFAULT_CITY_NAME);
                Global.LOCATION_FAIL_TAG = true;
            }
            //获取定位结果
            if(location!=null) {
                if (mOnLocateCompletedListener != null) {
                    mOnLocateCompletedListener.onLocateCompleted(
                            location.getLatitude(),
                            location.getLongitude(),
                            location);
                }
            }
            stopLocation();
        }
    }

    public void destory(){
        stopLocation();
        if (mBDLocationClient != null && mBDLocationListener != null) {
            mBDLocationClient.unRegisterLocationListener(mBDLocationListener);
        }
    }
}

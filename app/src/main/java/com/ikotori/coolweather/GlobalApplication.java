package com.ikotori.coolweather;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.ikotori.coolweather.baidumap.LocationService;
import com.socks.library.KLog;

/**
 * Created by Fashion at 2018/04/21 23:17.
 * Describe:
 */

public class GlobalApplication extends Application {


    public LocationService mLocationService;
    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(BuildConfig.DEBUG);
        //初始化定位SDK
        mLocationService = new LocationService(getApplicationContext());
        // 初始化百度地图sdk
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }
}

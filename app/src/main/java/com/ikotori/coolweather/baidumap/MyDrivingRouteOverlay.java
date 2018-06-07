package com.ikotori.coolweather.baidumap;

import android.content.Context;

import com.baidu.mapapi.map.BaiduMap;

/**
 * Created by Fashion at 2018/05/20 18:37.
 * Describe:
 */

public class MyDrivingRouteOverlay extends DrivingRouteOverlay {
    /**
     * 构造函数
     *
     * @param baiduMap 该DrivingRouteOvelray引用的 BaiduMap
     * @param context
     */
    public MyDrivingRouteOverlay(BaiduMap baiduMap, Context context) {
        super(baiduMap, context);
    }


}

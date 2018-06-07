package com.ikotori.coolweather.data;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.ikotori.coolweather.baidumap.LocationService;

/**
 * Created by Fashion at 2018/05/26 22:08.
 * Describe:
 */

public class BaiduLocationDataSource {

    public static BaiduLocationDataSource INSTANCE;

    public final LocationService mLocationService;

    private BDAbstractLocationListener mListener;

    private BaiduLocationDataSource(LocationService locationService) {
        mLocationService = locationService;
    }

    public static BaiduLocationDataSource getInstance(LocationService locationService) {
        if (INSTANCE == null) {
            synchronized (BaiduLocationDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BaiduLocationDataSource(locationService);
                }
            }
        }
        return INSTANCE;
    }

    public interface LocationCallback {
        void fail();

        void success(BDLocation location);
    }

    public void start(final LocationCallback callback) {
        if (mListener == null) {
            mListener = new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    if (null == bdLocation || BDLocation.TypeServerError == bdLocation.getLocType()) {
                        callback.fail();
                    } else {
                        callback.success(bdLocation);
                    }
                }
            };
        }
        mLocationService.setLocationOption(mLocationService.getDefaultLocationClientOption());
        mLocationService.registerListener(mListener);
        mLocationService.start();
    }

    public void stop() {
        mLocationService.unregisterListener(mListener);
        mLocationService.stop();
    }
}

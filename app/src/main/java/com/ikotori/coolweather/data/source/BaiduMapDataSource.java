package com.ikotori.coolweather.data.source;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.entity.CityWeather;

import java.util.List;

/**
 * Created by Fashion at 2018/05/09 22:24.
 * Describe:
 */

public interface BaiduMapDataSource {

    interface RoutesCallback {
        void onRoutesLoaded(String routes);

        void onDataNotAvailable();
    }


    interface RoutesToLocationsCallback {
        void onLocationsLoaded(List<String> cids);

        void onLoadFailed();
    }

    interface CityWeathersCallback {
        void onCityWeathersLoaded(List<CityWeather> weathers);

        void onLoadFailed();
    }

    void getRoutes(@NonNull String originLon, @NonNull String originLat, @NonNull String destinationLon, @NonNull String destinationLat, @NonNull RoutesCallback callback);

    void getLocations(@NonNull String routes, @NonNull RoutesToLocationsCallback callback);

    void getCityWeathers(@NonNull List<String> cids, @NonNull CityWeathersCallback callback);

}

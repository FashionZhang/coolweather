package com.ikotori.coolweather.data.source.repository;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.source.BaiduMapDataSource;
import com.ikotori.coolweather.data.source.QueryDataSource;

import java.util.List;

/**
 * Created by Fashion at 2018/05/10 15:27.
 * Describe:
 */

public class TravelWeatherRepository implements BaiduMapDataSource,QueryDataSource {

    private static TravelWeatherRepository INSTANCE = null;

    private final BaiduMapDataSource mRemoteDataSource;

    private final QueryDataSource mQueryRemoteDataSource;

    public static TravelWeatherRepository getInstance(BaiduMapDataSource mRemoteDataSource,QueryDataSource mQueryRemoteDataSource) {
        if (INSTANCE == null) {
            synchronized (TravelWeatherRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TravelWeatherRepository(mRemoteDataSource, mQueryRemoteDataSource);
                }
            }
        }
        return INSTANCE;
    }

    private TravelWeatherRepository(BaiduMapDataSource mRemoteDataSource, QueryDataSource mQueryRemoteDataSource) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mQueryRemoteDataSource = mQueryRemoteDataSource;
    }

    @Override
    public void getRoutes(@NonNull String originLon, @NonNull String originLat, @NonNull String destinationLon, @NonNull String destinationLat, @NonNull RoutesCallback callback) {
        mRemoteDataSource.getRoutes(originLon, originLat, destinationLon, destinationLat, callback);
    }

    @Override
    public void getLocations(@NonNull String routes, @NonNull RoutesToLocationsCallback callback) {
        mRemoteDataSource.getLocations(routes, callback);
    }

    @Override
    public void getCityWeathers(@NonNull List<String> cids, @NonNull CityWeathersCallback callback) {
        mRemoteDataSource.getCityWeathers(cids, callback);
    }

    @Override
    public void getQueryResult(@NonNull String query, @NonNull QueryResultCallback callback) {
        mQueryRemoteDataSource.getQueryResult(query, callback);
    }
}

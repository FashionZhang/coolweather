package com.ikotori.coolweather.data.source.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.QueryDataSource;
import com.ikotori.coolweather.data.source.remote.QueryRemoteDataSource;

/**
 * Created by Fashion at 2018/04/21 15:03.
 * Describe:
 */

public class CitiesRepository implements CitiesDataSource,QueryDataSource{

    private static CitiesRepository INSTANCE = null;

    private final QueryDataSource mQueryRemoteDataSource;

    private final CitiesDataSource mLocalDataSource;

    private CitiesRepository(@Nullable QueryDataSource remote, @NonNull CitiesDataSource local) {
        mLocalDataSource = local;
        mQueryRemoteDataSource = remote;
    }

    public static CitiesRepository getInstance(@Nullable QueryDataSource remote, @NonNull CitiesDataSource local) {
        if (INSTANCE == null) {
            INSTANCE = new CitiesRepository(remote, local);
        }
        return INSTANCE;
    }

    @Override
    public void getQueryResult(@NonNull String query, @NonNull QueryResultCallback callback) {
        if (null == mQueryRemoteDataSource) {

        } else {
            mQueryRemoteDataSource.getQueryResult(query,callback);
        }
    }

    @Override
    public void getCities(@NonNull LoadCitiesCallback callback) {
        mLocalDataSource.getCities(callback);
    }

    @Override
    public void insertCity(@NonNull QueryItem city, @NonNull InsertCityCallback callback) {
        mLocalDataSource.insertCity(city, callback);
    }

    @Override
    public void updateCity(@NonNull QueryItem city, @NonNull UpdateCityCallback callback) {
        mLocalDataSource.updateCity(city, callback);
    }

    @Override
    public void changeHomeCity(@NonNull QueryItem city, @NonNull QueryItem originCity, @NonNull UpdateCityCallback callback) {
        mLocalDataSource.changeHomeCity(city, originCity, callback);
    }

    @Override
    public void deleteCity(@NonNull String cid, @NonNull DeleteCityCallback callback) {
        mLocalDataSource.deleteCity(cid, callback);
    }
}

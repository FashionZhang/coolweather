package com.ikotori.coolweather.data.source.repository;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.WeatherDataSource;

/**
 * Created by Fashion at 2018/04/22 11:08.
 * Describe:
 */

public class WeatherHomeRepository implements CitiesDataSource, WeatherDataSource {

    private static WeatherHomeRepository INSTANCE = null;

    private final CitiesDataSource mCitiesDataSource;

    private final WeatherDataSource mLocalWeatherDataSource;

    private final WeatherDataSource mRemoteWeatherDataSource;

    private WeatherHomeRepository(@NonNull CitiesDataSource citiesDataSource, @NonNull WeatherDataSource local, @NonNull WeatherDataSource remote) {
        mCitiesDataSource = citiesDataSource;
        mLocalWeatherDataSource = local;
        mRemoteWeatherDataSource = remote;
    }

    public static WeatherHomeRepository getInstance(@NonNull CitiesDataSource citiesDataSource, @NonNull WeatherDataSource local, @NonNull WeatherDataSource remote) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherHomeRepository(citiesDataSource, local, remote);
        }
        return INSTANCE;
    }


    @Override
    public void getCities(@NonNull LoadCitiesCallback callback) {
        mCitiesDataSource.getCities(callback);
    }

    @Override
    public void insertCity(@NonNull QueryItem city, @NonNull InsertCityCallback callback) {

    }

    @Override
    public void updateCity(@NonNull QueryItem city, @NonNull UpdateCityCallback callback) {

    }

    @Override
    public void changeHomeCity(@NonNull QueryItem city, @NonNull QueryItem originCity, @NonNull UpdateCityCallback callback) {

    }

    @Override
    public void deleteCity(@NonNull String cid, @NonNull DeleteCityCallback callback) {

    }

    @Override
    public void loadWeatherNow(String cid, LoadWeatherNowCallback callback) {
        mRemoteWeatherDataSource.loadWeatherNow(cid, callback);
    }

    @Override
    public void insertWeatherNow(WeatherNow now) {
        mLocalWeatherDataSource.insertWeatherNow(now);
    }
}

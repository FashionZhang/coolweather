package com.ikotori.coolweather.data.source.local;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.WeatherDataSource;
import com.ikotori.coolweather.util.AppExecutors;

/**
 * Created by Fashion at 2018/04/22 13:03.
 * Describe:
 */

public class WeatherLocalDataSource implements WeatherDataSource {

    private static WeatherLocalDataSource INSTANCE;

    private AppExecutors mAppExecutors;


    public static WeatherLocalDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (null == INSTANCE) {
            synchronized (WeatherLocalDataSource.class) {
                if (null == INSTANCE) {
                    INSTANCE = new WeatherLocalDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    private WeatherLocalDataSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    @Override
    public void loadWeatherNow(String cid, LoadWeatherNowCallback callback) {

    }

    @Override
    public void insertWeatherNow(WeatherNow now) {

    }
}

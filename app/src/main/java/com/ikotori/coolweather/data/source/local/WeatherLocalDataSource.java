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

    private WeatherDao mWeatherDao;

    public static WeatherLocalDataSource getInstance(@NonNull AppExecutors appExecutors, @NonNull WeatherDao weatherDao) {
        if (null == INSTANCE) {
            synchronized (WeatherLocalDataSource.class) {
                if (null == INSTANCE) {
                    INSTANCE = new WeatherLocalDataSource(appExecutors, weatherDao);
                }
            }
        }
        return INSTANCE;
    }

    private WeatherLocalDataSource(@NonNull AppExecutors appExecutors, @NonNull WeatherDao weatherDao) {
        mAppExecutors = appExecutors;
        mWeatherDao = weatherDao;
    }

    @Override
    public void loadWeatherNow(final String cid, final LoadWeatherNowCallback callback) {
        Runnable runnable =new Runnable() {
            @Override
            public void run() {
                final WeatherNow now = mWeatherDao.getCityWeatherNow(cid);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (now == null) {
                            callback.loadWeatherNowFail();
                        } else {
                            callback.loadWeatherNowSuccess(now);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void insertWeatherNow(final WeatherNow now) {
        Runnable runnable =new Runnable() {
            @Override
            public void run() {
                mWeatherDao.insertCityWeatherNow(now);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }
}

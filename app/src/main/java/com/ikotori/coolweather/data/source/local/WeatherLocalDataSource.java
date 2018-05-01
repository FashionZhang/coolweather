package com.ikotori.coolweather.data.source.local;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.entity.AirNow;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherHourly;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.WeatherDataSource;
import com.ikotori.coolweather.util.AppExecutors;

import java.util.List;

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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final WeatherNow now = mWeatherDao.getCityWeatherNow(cid);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (now == null) {
                            callback.loadWeatherNowFailed();
                        } else {
                            callback.loadWeatherNowSucceeded(now);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void insertWeatherNow(final WeatherNow now) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mWeatherDao.insertCityWeatherNow(now);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void loadWeatherForecasts(final String cid, final LoadWeatherForecastCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<WeatherForecast> forecasts = mWeatherDao.getWeatherForecastsByCid(cid);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (forecasts != null && forecasts.size() > 0) {
                            callback.loadWeatherForecastSucceeded(forecasts);
                        } else {
                            callback.loadWeatherForecastFailed();
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void insertWeatherForecasts(final List<WeatherForecast> forecasts) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mWeatherDao.insertWeatherForecasts(forecasts);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteWeatherForecasts(final List<WeatherForecast> forecasts) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mWeatherDao.deleteWeatherForecasts(forecasts);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void loadWeatherHourlies(final String cid, final LoadWeatherHourlyCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<WeatherHourly> hourlies = mWeatherDao.getWeatherHourliesByCid(cid);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (hourlies != null && hourlies.size() > 0) {
                            callback.loadWeatherHourliesSucceeded(hourlies);
                        } else {
                            callback.loadWeatherHourliesFailed();
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void insertWeatherHourlies(final List<WeatherHourly> hourlies) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mWeatherDao.insertWeatherHourlies(hourlies);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteWeatherHourlies(final List<WeatherHourly> hourlies) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mWeatherDao.deleteWeatherHourlies(hourlies);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void loadAirNow(final String cid, final LoadAirNowCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final AirNow now = mWeatherDao.getAirNowByCid(cid);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (now != null) {
                            callback.loadAirNowSucceeded(now);
                        } else {
                            callback.loadAirNowFailed();
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void insertAirNow(final AirNow now) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mWeatherDao.insertAirNow(now);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteAirNow(final AirNow now) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mWeatherDao.deleteAirNow(now);
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }
}

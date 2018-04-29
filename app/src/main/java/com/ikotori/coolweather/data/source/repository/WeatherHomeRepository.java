package com.ikotori.coolweather.data.source.repository;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.Weather;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.WeatherDataSource;
import com.socks.library.KLog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Fashion at 2018/04/22 11:08.
 * Describe:
 */

public class WeatherHomeRepository implements CitiesDataSource, WeatherDataSource {

    private static WeatherHomeRepository INSTANCE = null;

    private final CitiesDataSource mCitiesDataSource;

    private final WeatherDataSource mLocalWeatherDataSource;

    private final WeatherDataSource mRemoteWeatherDataSource;

    //内存缓存数据
    private Map<String, Weather> mCacheWeather;

    //记录缓存数据是否可用,不可用时需要从远程加载数据
    //只有当用户触发有效刷新时或者本地数据已过期才会置为true
    //以下boolean类型变量都是这种用途
    private boolean mCacheWeatherIsInvalid = false;

    private boolean mCacheWeatherNowIsInvalid = false;

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
    public void loadWeatherNow(final String cid, final LoadWeatherNowCallback callback) {
        Weather weather;
        WeatherNow weatherNow;
        if (mCacheWeather != null && !mCacheWeatherNowIsInvalid
                && (weather = (Weather) mCacheWeather.get(cid)) != null
                && (weatherNow = weather.getNow()) != null) {

            callback.loadWeatherNowSuccess(weatherNow);
            return;
        }
        if (mCacheWeatherNowIsInvalid) {
            //如果缓存被标记为不可获得的话需要从远程获取数据
            //这种情况发生在用户触发刷新时或者本地数据已过期时
            getWeatherNowFromRemote(cid, callback);
            KLog.d("远程");
        } else {
            KLog.d("本地");
            mLocalWeatherDataSource.loadWeatherNow(cid, new LoadWeatherNowCallback() {
                @Override
                public void loadWeatherNowSuccess(WeatherNow now) {
                    refreshCache(now, cid);
                    callback.loadWeatherNowSuccess(now);
                }

                @Override
                public void loadWeatherNowFail() {
                    getWeatherNowFromRemote(cid, callback);
                }
            });
        }
    }

    private void getWeatherNowFromRemote(@NonNull final String cid, @NonNull final LoadWeatherNowCallback callback) {
        KLog.d("远程");
        mRemoteWeatherDataSource.loadWeatherNow(cid, new LoadWeatherNowCallback() {
            @Override
            public void loadWeatherNowSuccess(WeatherNow now) {
                refreshCacheAndLocalDataSource(now, cid);
                callback.loadWeatherNowSuccess(now);
            }

            @Override
            public void loadWeatherNowFail() {
                callback.loadWeatherNowFail();
            }
        });
    }

    @Override
    public void insertWeatherNow(WeatherNow now) {
        mLocalWeatherDataSource.insertWeatherNow(now);
    }


    private void refreshCacheAndLocalDataSource(Object o, @NonNull String cid) {
        if (mCacheWeather == null) {
            mCacheWeather = new HashMap<>();
        }
        Weather weather = new Weather();
        if (mCacheWeather.get(cid) != null) {
            weather =  mCacheWeather.get(cid);
        }
        if (o instanceof WeatherNow) {
            weather.setNow((WeatherNow) o);
            mCacheWeatherNowIsInvalid = false;
            mLocalWeatherDataSource.insertWeatherNow((WeatherNow) o);
        }
        mCacheWeather.put(cid, weather);
    }

    private void refreshCache(Object o, @NonNull String cid) {
        if (mCacheWeather == null) {
            mCacheWeather = new HashMap<>();
        }
        Weather weather = new Weather();
        if (mCacheWeather.containsKey(cid)) {
            weather = mCacheWeather.get(cid);
        }
        if (o instanceof WeatherNow) {
            weather.setNow((WeatherNow) o);
            mCacheWeatherNowIsInvalid = false;
        }
        mCacheWeather.put(cid, weather);
    }

}

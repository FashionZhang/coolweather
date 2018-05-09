package com.ikotori.coolweather.data.source.repository;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.AirNow;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherHourly;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.QueryDataSource;
import com.ikotori.coolweather.data.source.WeatherDataSource;

import java.util.List;
import java.util.Map;

/**
 * Created by Fashion at 2018/05/07 14:29.
 * Describe:
 */

public class QuickWeatherRepository implements QueryDataSource, WeatherDataSource {

    private static QuickWeatherRepository INSTANCE = null;

    private final QueryDataSource mQueryRemoteDataSource;

    private final WeatherDataSource mRemoteWeatherDataSource;

    private Map<String, Object> mCache;

    public static QuickWeatherRepository getInstance(QueryDataSource queryDataSource, WeatherDataSource weatherDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new QuickWeatherRepository(queryDataSource, weatherDataSource);
        }
        return INSTANCE;
    }

    private QuickWeatherRepository(QueryDataSource mQueryRemoteDataSource, WeatherDataSource mRemoteWeatherDataSource) {
        this.mQueryRemoteDataSource = mQueryRemoteDataSource;
        this.mRemoteWeatherDataSource = mRemoteWeatherDataSource;
    }

    @Override
    public void loadWeatherNow(String cid, final LoadWeatherNowCallback callback) {
        mRemoteWeatherDataSource.loadWeatherNow(cid, new LoadWeatherNowCallback() {
            @Override
            public void loadWeatherNowSucceeded(WeatherNow now) {
                callback.loadWeatherNowSucceeded(now);
            }

            @Override
            public void loadWeatherNowFailed() {
                callback.loadWeatherNowFailed();
            }
        });
    }

    @Override
    public void insertWeatherNow(WeatherNow now) {

    }

    @Override
    public void loadWeatherForecasts(String cid, LoadWeatherForecastCallback callback) {

    }

    @Override
    public void insertWeatherForecasts(List<WeatherForecast> forecasts) {

    }

    @Override
    public void deleteWeatherForecasts(List<WeatherForecast> forecasts) {

    }

    @Override
    public void loadWeatherHourlies(String cid, LoadWeatherHourlyCallback callback) {

    }

    @Override
    public void insertWeatherHourlies(List<WeatherHourly> hourlies) {

    }

    @Override
    public void deleteWeatherHourlies(List<WeatherHourly> hourlies) {

    }

    @Override
    public void loadAirNow(String cid, LoadAirNowCallback callback) {

    }

    @Override
    public void insertAirNow(AirNow now) {

    }

    @Override
    public void deleteAirNow(AirNow now) {

    }

    @Override
    public void getQueryResult(@NonNull String query, @NonNull final QueryResultCallback callback) {
        mQueryRemoteDataSource.getQueryResult(query, new QueryResultCallback() {
            @Override
            public void onQueryResultLoaded(List<QueryItem> queryItems) {
                callback.onQueryResultLoaded(queryItems);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }
}

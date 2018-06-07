package com.ikotori.coolweather.home.quickweather;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.QueryDataSource;
import com.ikotori.coolweather.data.source.WeatherDataSource;
import com.ikotori.coolweather.data.source.repository.QuickWeatherRepository;

import java.util.List;

/**
 * Created by Fashion at 2018/05/06 18:58.
 * Describe:
 */

public class QuickWeatherPresenter implements QuickWeatherContract.Presenter {

    private final QuickWeatherContract.View mQuickView;

    private final QuickWeatherRepository mRepository;

    public QuickWeatherPresenter(@NonNull QuickWeatherContract.View view, QuickWeatherRepository repository) {
        mQuickView = view;
        mRepository = repository;
        view.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void queryCity(@NonNull String query) {
        mRepository.getQueryResult(query, new QueryDataSource.QueryResultCallback() {
            @Override
            public void onQueryResultLoaded(List<QueryItem> queryItems) {
                mQuickView.showMatchCities(queryItems);
            }

            @Override
            public void onDataNotAvailable() {
                mQuickView.showNoMatchCity();
            }
        });
    }

    @Override
    public void citySelect(QueryItem city) {
        mQuickView.showCitySelect(city);
    }

    @Override
    public void queryWeather(@NonNull String cid) {
        mRepository.loadWeatherNow(cid, new WeatherDataSource.LoadWeatherNowCallback() {
            @Override
            public void loadWeatherNowSucceeded(WeatherNow now) {
                mQuickView.showWeather(now);
            }

            @Override
            public void loadWeatherNowFailed() {
                mQuickView.showWeatherNotAvailable();
            }
        });
    }
}

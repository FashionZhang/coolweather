package com.ikotori.coolweather.home.travelweather;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.CityWeather;
import com.ikotori.coolweather.data.source.BaiduMapDataSource;
import com.ikotori.coolweather.data.source.QueryDataSource;
import com.ikotori.coolweather.data.source.repository.TravelWeatherRepository;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by Fashion at 2018/05/06 19:05.
 * Describe:
 */

public class TravelWeatherPresenter implements TravelWeatherContract.Presenter {

    private final TravelWeatherContract.View mView;

    private final TravelWeatherRepository mRepository;

    public TravelWeatherPresenter(@NonNull TravelWeatherContract.View view, TravelWeatherRepository mRepository) {
        this.mView = view;
        this.mRepository = mRepository;
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
                mView.showMatchCities(queryItems);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNoMatchCity();
            }
        });
    }

    @Override
    public void queryWeathers(@NonNull String originLon, @NonNull String originLat, @NonNull String destinationLon, @NonNull String destinationLat) {
        mRepository.getRoutes(originLon, originLat, destinationLon, destinationLat, new BaiduMapDataSource.RoutesCallback() {
            @Override
            public void onRoutesLoaded(String routes) {
                mRepository.getLocations(routes, new BaiduMapDataSource.RoutesToLocationsCallback() {
                    @Override
                    public void onLocationsLoaded(List<String> cids) {
                        mRepository.getCityWeathers(cids, new BaiduMapDataSource.CityWeathersCallback() {
                            @Override
                            public void onCityWeathersLoaded(List<CityWeather> weathers) {
                                mView.showWeathers(weathers);
                            }

                            @Override
                            public void onLoadFailed() {
                                mView.showNoWeather();
                            }
                        });
                    }

                    @Override
                    public void onLoadFailed() {
                        mView.showNoWeather();
                    }
                });
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNoWeather();
            }
        });
    }
}

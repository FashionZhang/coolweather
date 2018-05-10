package com.ikotori.coolweather.home.travelweather;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.BasePresenter;
import com.ikotori.coolweather.BaseView;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.CityWeather;

import java.util.List;

/**
 * Created by Fashion at 2018/05/06 19:04.
 * Describe:
 */

public interface TravelWeatherContract {

    interface View extends BaseView<Presenter> {
        void showMatchCities(List<QueryItem> resultList);

        void showNoMatchCity();

        void showWeathers(List<CityWeather> weathers);

        void showNoWeather();

    }

    interface Presenter extends BasePresenter {

        void queryCity(@NonNull String query);

        void queryWeathers(@NonNull String originLon, @NonNull String originLat, @NonNull String destinationLon, @NonNull String destinationLat);
    }

}

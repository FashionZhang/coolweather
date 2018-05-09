package com.ikotori.coolweather.home.quickweather;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.BasePresenter;
import com.ikotori.coolweather.BaseView;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.WeatherNow;

import java.util.List;

/**
 * Created by Fashion at 2018/05/06 18:57.
 * Describe:
 */

public interface QuickWeatherContract {

    interface View extends BaseView<Presenter> {

        void showMatchCities(List<QueryItem> resultList);

        void showNoMatchCity();

        void showCitySelect(QueryItem city);

        void showWeather(WeatherNow now);

        void showWeatherNotAvailable();

        void showGuideView();
    }

    interface Presenter extends BasePresenter {

        void queryCity(@NonNull String query);

        void citySelect(QueryItem city);

        void queryWeather(@NonNull String cid);

    }

}

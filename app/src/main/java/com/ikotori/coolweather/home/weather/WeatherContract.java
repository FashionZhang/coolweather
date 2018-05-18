package com.ikotori.coolweather.home.weather;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ikotori.coolweather.BasePresenter;
import com.ikotori.coolweather.BaseView;
import com.ikotori.coolweather.data.entity.AirNow;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherHourly;
import com.ikotori.coolweather.data.entity.WeatherNow;

import java.util.List;

/**
 * Created by Fashion at 2018/04/26 21:28.
 * Describe:
 */

public interface WeatherContract {

    public interface View extends BaseView<Presenter> {

        void showDataNotAvailableUi();

        void weatherNowLoaded(WeatherNow weatherNow);

        void weatherNowNotAvailable();

        void weatherForecastsLoaded(List<WeatherForecast> forecasts);

        void weatherForecastsNotAvailable();

        void WeatherHourliesLoaded(List<WeatherHourly> hourlies);

        void WeatherHourliesNotAvailable();

        void AirNowLoaded(AirNow now);

        void AirNowNotAvailable();

        void changeToolBarTitle();

        void showMoreWeather(@Nullable String location);
    }

    public interface Presenter extends BasePresenter {
        void start(@NonNull String cid, @NonNull View view);

        void loadWeatherNow(@NonNull String cid, @NonNull View view);

        void loadWeatherForecast(@NonNull String cid, @NonNull View view);

        void loadWeatherHourlies(@NonNull String cid, @NonNull View view);

        void loadAirNow(@NonNull String cid, @NonNull View view);

        void watchMoreWeather(@Nullable String location, @NonNull View view);

        void refresh(@NonNull String cid, @NonNull View view);
    }
}

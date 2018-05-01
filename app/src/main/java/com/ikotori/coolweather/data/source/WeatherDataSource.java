package com.ikotori.coolweather.data.source;

import com.ikotori.coolweather.data.entity.AirNow;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherHourly;
import com.ikotori.coolweather.data.entity.WeatherNow;

import java.util.List;

/**
 * Created by Fashion at 2018/04/22 11:14.
 * Describe:
 */

public interface WeatherDataSource {

    interface LoadWeatherNowCallback {
        void loadWeatherNowSucceeded(WeatherNow now);

        void loadWeatherNowFailed();
    }

    interface LoadWeatherForecastCallback {
        void loadWeatherForecastSucceeded(List<WeatherForecast> forecasts);

        void loadWeatherForecastFailed();
    }

    interface LoadWeatherHourlyCallback {
        void loadWeatherHourliesSucceeded(List<WeatherHourly> hourlies);

        void loadWeatherHourliesFailed();
    }

    interface LoadAirNowCallback {
        void loadAirNowSucceeded(AirNow now);

        void loadAirNowFailed();
    }

    void loadWeatherNow(String cid, LoadWeatherNowCallback callback);

    void insertWeatherNow(WeatherNow now);

    void loadWeatherForecasts(String cid, LoadWeatherForecastCallback callback);

    void insertWeatherForecasts(List<WeatherForecast> forecasts);

    void deleteWeatherForecasts(List<WeatherForecast> forecasts);

    void loadWeatherHourlies(String cid, LoadWeatherHourlyCallback callback);

    void insertWeatherHourlies(List<WeatherHourly> hourlies);

    void deleteWeatherHourlies(List<WeatherHourly> hourlies);

    void loadAirNow(String cid, LoadAirNowCallback callback);

    void insertAirNow(AirNow now);

    void deleteAirNow(AirNow now);
}

package com.ikotori.coolweather.data.source;

import com.ikotori.coolweather.data.entity.WeatherNow;

/**
 * Created by Fashion at 2018/04/22 11:14.
 * Describe:
 */

public interface WeatherDataSource {

    interface LoadWeatherNowCallback {
        void loadWeatherNowSuccess(WeatherNow now);

        void loadWeatherNowFail();
    }


    void loadWeatherNow(String cid, LoadWeatherNowCallback callback);

    void insertWeatherNow(WeatherNow now);

}

package com.ikotori.coolweather.home.weather;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.BasePresenter;
import com.ikotori.coolweather.BaseView;
import com.ikotori.coolweather.data.entity.WeatherNow;

/**
 * Created by Fashion at 2018/04/26 21:28.
 * Describe:
 */

public interface WeatherContract {

    public interface View extends BaseView<Presenter> {

        void showDataNotAvailableUi();

        void weatherNowLoaded(WeatherNow weatherNow);

        void weatherNowNotAvailable();

    }

    public interface Presenter extends BasePresenter {
        void start(@NonNull String cid, @NonNull View view);

        void loadWeatherNow(@NonNull String cid, @NonNull View view);

    }
}

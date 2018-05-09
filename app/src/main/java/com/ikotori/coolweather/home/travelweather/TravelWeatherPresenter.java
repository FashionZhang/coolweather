package com.ikotori.coolweather.home.travelweather;

import android.support.annotation.NonNull;

/**
 * Created by Fashion at 2018/05/06 19:05.
 * Describe:
 */

public class TravelWeatherPresenter implements TravelWeatherContract.Presenter {

    private final TravelWeatherContract.View mView;

    public TravelWeatherPresenter(@NonNull TravelWeatherContract.View view) {
        this.mView = view;

    }

    @Override
    public void start() {

    }
}

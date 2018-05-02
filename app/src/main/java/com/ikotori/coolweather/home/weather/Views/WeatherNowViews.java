package com.ikotori.coolweather.home.weather.Views;

import android.view.View;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherNow;

/**
 * Created by Fashion at 2018/05/02 20:58.
 * Describe:
 */

public class WeatherNowViews {
    public TextView mWeatherNowView;

    public TextView mWeatherNowDegreeView;

    public TextView mNowCondView;

    public TextView mNowTmpMaxView;

    public TextView mNowTmpMinView;

    public WeatherNowViews(View view) {
        mWeatherNowView = view.findViewById(R.id.weather_now);
        mWeatherNowDegreeView = view.findViewById(R.id.weather_now_degree);
        mNowCondView = view.findViewById(R.id.now_cond);
        mNowTmpMaxView = view.findViewById(R.id.now_tmp_max);
        mNowTmpMinView = view.findViewById(R.id.now_tmp_min);
    }

    public void setWeatherNow(WeatherNow now) {
        mWeatherNowView.setText(now.getTmp());
        mNowCondView.setText(now.getCondTxt());
    }

    public void setWeatherTmp(WeatherForecast forecast) {
        mNowTmpMaxView.setText(forecast.tmpMax);
        mNowTmpMinView.setText(forecast.tmpMin);
    }
}

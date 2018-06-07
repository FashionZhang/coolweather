package com.ikotori.coolweather.home.weather.Views;

import android.view.View;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.util.ActivityUtils;
import com.ikotori.coolweather.util.StringUtil;

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

    public TextView mNowTmpUnitView;

    public WeatherNowViews(View view) {
        mWeatherNowView = view.findViewById(R.id.weather_now);
        mWeatherNowDegreeView = view.findViewById(R.id.weather_now_degree);
        mNowCondView = view.findViewById(R.id.now_cond);
        mNowTmpMaxView = view.findViewById(R.id.now_tmp_max);
        mNowTmpMinView = view.findViewById(R.id.now_tmp_min);
        mNowTmpUnitView = view.findViewById(R.id.now_tmp_unit);
    }

    public void setWeatherNow(WeatherNow now) {
        if (ActivityUtils.getTemperatureUnit(mWeatherNowView.getContext()) == 2) {
            mWeatherNowView.setText(StringUtil.celsiusToFahrenheit(now.getTmp()));
            mNowTmpUnitView.setText(R.string.temperature_degree_fahrenheit);
        } else {
            mWeatherNowView.setText(now.getTmp());
            mNowCondView.setText(now.getCondTxt());
            mNowTmpUnitView.setText(R.string.temperature_degree_celsius);
        }


    }

    public void setWeatherTmp(WeatherForecast forecast) {
        if (ActivityUtils.getTemperatureUnit(mNowTmpMaxView.getContext()) == 2) {
            mNowTmpMinView.setText(StringUtil.celsiusToFahrenheit(forecast.tmpMin));
            mNowTmpMaxView.setText(StringUtil.celsiusToFahrenheit(forecast.tmpMax));
        } else {
            mNowTmpMaxView.setText(String.valueOf(forecast.tmpMax));
            mNowTmpMinView.setText(String.valueOf(forecast.tmpMin));
        }
    }
}

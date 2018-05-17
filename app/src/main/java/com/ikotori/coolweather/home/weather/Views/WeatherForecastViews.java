package com.ikotori.coolweather.home.weather.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.util.ActivityUtils;
import com.ikotori.coolweather.util.DateUtil;
import com.ikotori.coolweather.util.StringUtil;

import java.util.List;

/**
 * Created by Fashion at 2018/05/02 21:54.
 * Describe:
 */

public class WeatherForecastViews {

    public LinearLayout mForecastContainer;

    public TextView mWatchMoreWeatherView;

    private WeatherForecastViewListener mListener;

    public WeatherForecastViews(View view, WeatherForecastViewListener listener) {
        mListener = listener;
        mForecastContainer = view.findViewById(R.id.forecast_container);
        mWatchMoreWeatherView = view.findViewById(R.id.watch_more_weather);
        mWatchMoreWeatherView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onWatcherMoreWeatherListener();
            }
        });
    }

    public void fillData(List<WeatherForecast> forecasts, Context context) {
        mForecastContainer.removeAllViews();
        for (WeatherForecast forecast : forecasts) {
            View forecastView = LayoutInflater.from(context).inflate(R.layout.item_weather_forecast, mForecastContainer, false);
            TextView forecastDate = forecastView.findViewById(R.id.forecast_date);
            TextView forecastCond = forecastView.findViewById(R.id.forecast_cond);
            TextView forecastTmpMax = forecastView.findViewById(R.id.forecast_tmp_max);
            TextView forecastTmpMin = forecastView.findViewById(R.id.forecast_tmp_min);
            forecastDate.setText(DateUtil.formatStringDate(forecast.date, DateUtil.FORMAT_YYYYMMDD));
            forecastCond.setText(String.valueOf(forecast.condTxtD));
            if (ActivityUtils.getTemperatureUnit(forecastView.getContext()) == 2) {
                forecastTmpMax.setText(StringUtil.celsiusToFahrenheit(forecast.tmpMax));
                forecastTmpMin.setText(StringUtil.celsiusToFahrenheit(forecast.tmpMin));
            } else {
                forecastTmpMax.setText(String.valueOf(forecast.tmpMax));
                forecastTmpMin.setText(String.valueOf(forecast.tmpMin));
            }
            mForecastContainer.addView(forecastView);
        }
    }

    public interface  WeatherForecastViewListener {
        void onWatcherMoreWeatherListener();
    }
}

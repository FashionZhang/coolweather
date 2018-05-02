package com.ikotori.coolweather.home.weather.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.util.DateUtil;

import java.util.List;

/**
 * Created by Fashion at 2018/05/02 21:54.
 * Describe:
 */

public class WeatherForecastViews {

    public LinearLayout mForecastContainer;

    public WeatherForecastViews(View view) {
        mForecastContainer = view.findViewById(R.id.forecast_container);
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
            forecastTmpMax.setText(String.valueOf(forecast.tmpMax));
            forecastTmpMin.setText(String.valueOf(forecast.tmpMin));
            mForecastContainer.addView(forecastView);
        }
    }
}

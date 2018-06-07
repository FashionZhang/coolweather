package com.ikotori.coolweather.home.weather.Views;

import android.view.View;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.AirNow;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.util.ActivityUtils;
import com.ikotori.coolweather.util.StringUtil;

/**
 * create by fashion at 2018/05/03 17:17
 * describe:
 */
public class AirNowViews {
    private TextView mAirNowQltyView;

    private TextView mAirNowAqiView;

    private TextView mAirNowMainView;

    private TextView mAirNowPm25View;

    private TextView mAirNowFlView;

    private TextView mAirNowWindView;

    private TextView mAirNowWindLabel;

    private TextView mAirNowHumView;

    private TextView mAirNowVisView;

    private TextView mAirNowPresView;

    private View root;

    public AirNowViews(View view) {
        root = view;
        mAirNowQltyView = view.findViewById(R.id.air_now_qlty_text);
        mAirNowAqiView = view.findViewById(R.id.air_now_aqi_text);
        mAirNowMainView = view.findViewById(R.id.air_now_main_text);
        mAirNowPm25View = view.findViewById(R.id.air_now_pm25_text);
        mAirNowFlView = view.findViewById(R.id.air_now_fl_text);
        mAirNowWindLabel = view.findViewById(R.id.air_now_wind_label);
        mAirNowWindView = view.findViewById(R.id.air_now_wind_text);
        mAirNowHumView = view.findViewById(R.id.air_now_hum_text);
        mAirNowVisView = view.findViewById(R.id.air_now_vis_text);
        mAirNowPresView = view.findViewById(R.id.air_now_pres_text);
    }

    public void setAirNowData(AirNow now) {
        mAirNowQltyView.setText(now.qlty);
        mAirNowAqiView.setText(now.aqi);
        mAirNowMainView.setText(now.main);
        mAirNowPm25View.setText(now.pm25);
    }

    public void setWeatherNowData(WeatherNow now) {
        if (ActivityUtils.getTemperatureUnit(root.getContext()) == 2) {
            mAirNowFlView.setText(StringUtil.celsiusToFahrenheit(now.getFl()));
        } else {
            mAirNowFlView.setText(now.getFl());
        }
        mAirNowWindLabel.setText(now.getWindDir());
        mAirNowWindView.setText(now.getWindSc());
        mAirNowHumView.setText(now.getHum());
        mAirNowVisView.setText(now.getVis());
        mAirNowPresView.setText(now.getPres());
    }
}

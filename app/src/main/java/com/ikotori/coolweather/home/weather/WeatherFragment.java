package com.ikotori.coolweather.home.weather;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.AirNow;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherHourly;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.home.weather.Views.AirNowViews;
import com.ikotori.coolweather.home.weather.Views.WeatherForecastViews;
import com.ikotori.coolweather.home.weather.Views.WeatherHourliesViews;
import com.ikotori.coolweather.home.weather.Views.WeatherNowViews;
import com.ikotori.coolweather.moreweather.MoreWeatherActivity;
import com.socks.library.KLog;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements WeatherContract.View {

    public final static String MORE_WEATHER_LOCATION = "location";
    public final static String CID = "cid";
    public final static String LOCATION = "location";
    public static final String IS_HOME = "isHome";
    public TextView mWeatherView;

    private String cid;
    private String location;
    private Boolean isHome;

    //天气信息更新时间
    private String updateTime;

    private boolean isVisible;

    private WeatherNowViews mWeatherNowViews;

    private WeatherForecastViews mWeatherForecastViews;

    private WeatherHourliesViews mWeatherHourliesViews;

    private AirNowViews mAirNowViews;

    private WeatherContract.Presenter mPresenter;
    public WeatherFragment() {
        // Required empty public constructor
    }


    public static WeatherFragment getInstance(String cid, String location, Boolean isHome) {
        Bundle arguments = new Bundle();
        arguments.putString(CID, cid);
        arguments.putString(LOCATION, location);
        arguments.putBoolean(IS_HOME, isHome);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        mWeatherView = root.findViewById(R.id.weather_now);
        Bundle intent = getArguments();
        cid = intent.getString(CID);
        location = intent.getString(LOCATION);
        isHome = intent.getBoolean(IS_HOME);

        mWeatherNowViews = new WeatherNowViews(root);
        mWeatherForecastViews = new WeatherForecastViews(root, mWeatherForecastListener);
        mWeatherHourliesViews = new WeatherHourliesViews(root);
        mAirNowViews = new AirNowViews(root);

        return root;
    }


    private WeatherForecastViews.WeatherForecastViewListener mWeatherForecastListener = new WeatherForecastViews.WeatherForecastViewListener() {
        @Override
        public void onWatcherMoreWeatherListener() {
            mPresenter.watchMoreWeather(location, WeatherFragment.this);
        }
    };

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisible = isVisibleToUser;
        if (isVisibleToUser) {
            lazyLoad();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyLoad() {
        if (isVisible && getView() != null) {
            mPresenter.start(cid, this);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        lazyLoad();
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isVisible && getView() != null;
    }

    @Override
    public void showDataNotAvailableUi() {
        mWeatherView.setText(cid);
    }

    @Override
    public void weatherNowLoaded(WeatherNow weatherNow) {
        mWeatherNowViews.setWeatherNow(weatherNow);
        mAirNowViews.setWeatherNowData(weatherNow);
        updateTime = weatherNow.getLoc();
        showToolBarTitle(location, weatherNow.getLoc(), isHome);
    }

    @Override
    public void weatherNowNotAvailable() {
//        mWeatherView.setText(getString(R.string.weather_now_not_available));
    }

    @Override
    public void weatherForecastsLoaded(List<WeatherForecast> forecasts) {
        KLog.d(forecasts);
        mWeatherNowViews.setWeatherTmp(forecasts.get(0));
        mWeatherForecastViews.fillData(forecasts, getActivity());
    }

    @Override
    public void weatherForecastsNotAvailable() {

    }

    @Override
    public void WeatherHourliesLoaded(List<WeatherHourly> hourlies) {
        mWeatherHourliesViews.setHourlies(hourlies);
    }

    @Override
    public void WeatherHourliesNotAvailable() {

    }

    @Override
    public void AirNowLoaded(AirNow now) {
        mAirNowViews.setAirNowData(now);
    }

    @Override
    public void AirNowNotAvailable() {

    }

    @Override
    public void changeToolBarTitle() {
        if (null == updateTime) {

        } else {
            showToolBarTitle(location,updateTime,isHome);
        }
    }

    @Override
    public void showMoreWeather(@Nullable String location) {
        Intent intent = new Intent(getContext(), MoreWeatherActivity.class);
        intent.putExtra(MORE_WEATHER_LOCATION, location);
        startActivity(intent);
    }

    private void showToolBarTitle(String title, String subTitle, boolean isHome) {
        KLog.d(title, subTitle, isHome);
        if (getParentFragment() instanceof ToolBarTitleListener) {
            ((ToolBarTitleListener) getParentFragment()).showToolbarTitle(title,subTitle,isHome);
        }
    }

    public interface ToolBarTitleListener {
        void showToolbarTitle(String title, String subTitle, Boolean isHome);
    }
}

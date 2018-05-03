package com.ikotori.coolweather.home.weather;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ikotori.coolweather.data.entity.AirNow;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherHourly;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.WeatherDataSource;
import com.ikotori.coolweather.data.source.repository.WeatherHomeRepository;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fashion at 2018/04/22 10:37.
 * Describe:
 */

public class WeatherPagerAdapterPresenter<T extends WeatherFragment> extends FragmentPagerAdapter implements WeatherContract.Presenter {

    private List<Fragment> mFragments = new ArrayList<>();

    private WeatherContract.View mCurrentView;

    private final WeatherHomeRepository mRepository;

    public WeatherPagerAdapterPresenter(FragmentManager fm, @NonNull WeatherHomeRepository repository) {
        super(fm);
        mRepository = repository;
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void setFragments(List<Fragment> fragments) {
        mFragments = fragments;
    }

    public void setNewFragments(List<Fragment> fragments) {
        mFragments.clear();
        mFragments = fragments;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentView = (T) object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        ((T) mFragments.get(position)).setPresenter(this);
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    /**
     * 判断当前view是否已经切换
     *
     * @param view
     * @return
     */
    private boolean isCurrentView(@Nullable WeatherContract.View view) {
        return mCurrentView == view;
//        return view.isActive();
    }

    @Override
    public void start() {
        mCurrentView.showDataNotAvailableUi();
    }

    @Override
    public void start(@NonNull String cid, @NonNull WeatherContract.View view) {
        loadWeatherNow(cid, view);
        loadWeatherForecast(cid, view);
        loadWeatherHourlies(cid, view);
        loadAirNow(cid, view);
    }

    @Override
    public void loadWeatherNow(@NonNull String cid, @NonNull final WeatherContract.View view) {
        mRepository.loadWeatherNow(cid, new WeatherDataSource.LoadWeatherNowCallback() {

            @Override
            public void loadWeatherNowSucceeded(WeatherNow now) {
                // TODO 内存缓存
                if (view.isActive()) {
                    view.weatherNowLoaded(now);
                }
            }

            @Override
            public void loadWeatherNowFailed() {
                if (view.isActive()) {
                    view.weatherNowNotAvailable();
                }
            }
        });
    }

    @Override
    public void loadWeatherForecast(@NonNull String cid, @NonNull final WeatherContract.View view) {
        mRepository.loadWeatherForecasts(cid, new WeatherDataSource.LoadWeatherForecastCallback() {
            @Override
            public void loadWeatherForecastSucceeded(List<WeatherForecast> forecasts) {
                if (view.isActive()) {
                    view.weatherForecastsLoaded(forecasts);

                }
            }

            @Override
            public void loadWeatherForecastFailed() {
                if (view.isActive()) {
                    view.weatherForecastsNotAvailable();
                }
            }
        });
    }

    @Override
    public void loadWeatherHourlies(@NonNull String cid, @NonNull final WeatherContract.View view) {
        mRepository.loadWeatherHourlies(cid, new WeatherDataSource.LoadWeatherHourlyCallback() {
            @Override
            public void loadWeatherHourliesSucceeded(List<WeatherHourly> hourlies) {
                if (view.isActive()) {
                    view.WeatherHourliesLoaded(hourlies);
                }
            }

            @Override
            public void loadWeatherHourliesFailed() {
                if (view.isActive()) {
                    view.WeatherHourliesNotAvailable();
                }
            }
        });
    }

    @Override
    public void loadAirNow(@NonNull String cid, @NonNull final WeatherContract.View view) {
        mRepository.loadAirNow(cid, new WeatherDataSource.LoadAirNowCallback() {
            @Override
            public void loadAirNowSucceeded(AirNow now) {
                if (view.isActive()) {
                    view.AirNowLoaded(now);
                }
            }

            @Override
            public void loadAirNowFailed() {
                if (view.isActive()) {
                    view.AirNowNotAvailable();
                }
            }
        });
    }

}

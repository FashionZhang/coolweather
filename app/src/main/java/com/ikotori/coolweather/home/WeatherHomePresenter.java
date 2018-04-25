package com.ikotori.coolweather.home;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.WeatherDataSource;
import com.ikotori.coolweather.data.source.repository.WeatherHomeRepository;

import java.util.List;

/**
 * Created by Fashion at 2018/04/18 22:09.
 * Describe:
 */

public class WeatherHomePresenter implements WeatherHomeContract.Presenter {

    private final WeatherHomeContract.View mHomeView;

    private final WeatherHomeRepository mHomeRepository;

    public WeatherHomePresenter(@NonNull WeatherHomeContract.View view, @NonNull WeatherHomeRepository repository) {
        mHomeView = view;
        mHomeView.setPresenter(this);
        mHomeRepository = repository;
    }

    @Override
    public void start() {
        loadAllCities();
    }

    @Override
    public void openCitySelect() {
        mHomeView.showCitySelectUi();
    }

    @Override
    public void openSetting() {
        mHomeView.showSettingUi();
    }

    @Override
    public void openShare() {
        mHomeView.showShareUi();
    }

    @Override
    public void loadWeatherNow(String cid) {
        mHomeRepository.loadWeatherNow(cid, new WeatherDataSource.LoadWeatherNowCallback() {
            @Override
            public void loadWeatherNowSuccess(WeatherNow now) {
                mHomeView.showWeatherNowUi(now);
            }

            @Override
            public void loadWeatherNowFail() {
                mHomeView.showNoWeatherUi();
            }
        });
    }

    @Override
    public void changeLocation(int position) {

    }

    @Override
    public void loadAllCities() {
        mHomeRepository.getCities(new CitiesDataSource.LoadCitiesCallback() {
            @Override
            public void onCitiesLoadSuccess(List<QueryItem> cities) {
                //将城市按照默认城市>城市添加顺序的方式重新排序
                if (cities.size() > 0) {
                    for (int i = 0; i < cities.size(); i++) {

                        QueryItem homeCity = cities.get(i);
                        if (homeCity.isHome()) {
                            cities.remove(i);
                            cities.add(0, homeCity);
                            break;
                        }
                    }
                    mHomeView.allCitiesLoaded(cities);
//                    loadWeatherNow(cities.get(0).getCid());
                }
            }

            @Override
            public void onDataNotAvailable() {
                mHomeView.showNoCityUi();
            }
        });
    }
}

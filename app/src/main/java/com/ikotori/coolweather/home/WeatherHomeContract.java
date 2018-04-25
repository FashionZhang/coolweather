package com.ikotori.coolweather.home;

import com.ikotori.coolweather.BasePresenter;
import com.ikotori.coolweather.BaseView;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.WeatherNow;

import java.util.List;

/**
 * Created by Fashion at 2018/04/18 21:53.
 * Describe:
 */

public interface WeatherHomeContract {

    interface View extends BaseView<Presenter> {
        void showCitySelectUi();

        void showSettingUi();

        void showShareUi();

        void showWeatherNowUi(WeatherNow weatherNow);

        void showNoWeatherUi();

        void showChangeLocation(int position);

        void allCitiesLoaded(List<QueryItem> cities);

        void showNoCityUi();
    }

    interface Presenter extends BasePresenter {
        void openCitySelect();

        void openSetting();

        void openShare();

        void loadWeatherNow(String cid);

        void changeLocation(int position);

        void loadAllCities();
    }
}

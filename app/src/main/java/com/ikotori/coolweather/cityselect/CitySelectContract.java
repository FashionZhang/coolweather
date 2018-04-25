package com.ikotori.coolweather.cityselect;

import com.ikotori.coolweather.BasePresenter;
import com.ikotori.coolweather.BaseView;
import com.ikotori.coolweather.data.QueryItem;

import java.util.List;

/**
 * Created by Fashion at 2018/04/19 00:01.
 * Describe:
 */

public interface CitySelectContract {

    interface View extends BaseView<Presenter> {
        void showCitySearchUi();

        void showCitySelectUi(QueryItem city);

        void showSetHomeUi(QueryItem city);

        void showDeleteCityUi(QueryItem city);

        void showFailUi();

        void showNoCity();

        void showCities(List<QueryItem> cities);
    }

    interface Presenter extends BasePresenter {
        void openCitySearch();

        void citySelect(QueryItem city);

        void setHome(QueryItem city, QueryItem originCity);

        void deleteCity(QueryItem city);

        void loadCities();
    }
}

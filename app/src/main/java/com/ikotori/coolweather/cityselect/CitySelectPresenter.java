package com.ikotori.coolweather.cityselect;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.repository.CitiesRepository;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by Fashion at 2018/04/19 00:04.
 * Describe:
 */

public class CitySelectPresenter implements CitySelectContract.Presenter {

    private final CitySelectContract.View mView;

    private final CitiesRepository mCitiesRepository;
    public CitySelectPresenter(@NonNull CitySelectContract.View view,@NonNull CitiesRepository citiesRepository) {
        mView = view;
        mView.setPresenter(this);
        mCitiesRepository = citiesRepository;
    }

    @Override
    public void start() {
        loadCities();
    }

    @Override
    public void openCitySearch() {
        mView.showCitySearchUi();
    }

    @Override
    public void citySelect(QueryItem city) {
        mView.showCitySelectUi(city);
    }

    @Override
    public void setHome(final QueryItem city, QueryItem originCity) {
        mCitiesRepository.changeHomeCity(city, originCity, new CitiesDataSource.UpdateCityCallback() {
            @Override
            public void onSetHomeSuccess() {
                mView.showSetHomeUi(city);
            }

            @Override
            public void onSetHomeFail() {

            }
        });
    }

    @Override
    public void deleteCity(final QueryItem city) {
        mCitiesRepository.deleteCity(city.getCid(), new CitiesDataSource.DeleteCityCallback() {
            @Override
            public void onDeleteSuccess() {
                mView.showDeleteCityUi(city);
            }

            @Override
            public void onDeleteFail() {
                mView.showFailUi();
            }
        });
    }

    @Override
    public void loadCities() {
        mCitiesRepository.getCities(new CitiesDataSource.LoadCitiesCallback() {
            @Override
            public void onCitiesLoadSuccess(List<QueryItem> cities) {
                mView.showCities(cities);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNoCity();
            }
        });
    }


}

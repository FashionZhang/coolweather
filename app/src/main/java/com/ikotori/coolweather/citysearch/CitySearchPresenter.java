package com.ikotori.coolweather.citysearch;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.QueryDataSource;
import com.ikotori.coolweather.data.source.repository.CitiesRepository;

import java.util.List;

/**
 * Created by Fashion at 2018/04/20 09:01.
 * Describe:
 */

public class CitySearchPresenter implements CitySearchContract.Presenter {

    private final CitySearchContract.View mView;

    private final CitiesRepository mCitiesRepository;

    public CitySearchPresenter(@NonNull CitySearchContract.View view, @NonNull CitiesRepository repository) {
        mView = view;
        mView.setPresenter(this);
        mCitiesRepository = repository;
    }

    @Override
    public void start() {
        mView.showNoMatchResult();
    }

    @Override
    public void queryCity(@NonNull String query) {
        mCitiesRepository.getQueryResult(query, new QueryDataSource.QueryResultCallback() {
            @Override
            public void onQueryResultLoaded(List<QueryItem> queryItems) {
                mView.showMatchResult(queryItems);
            }

            @Override
            public void onDataNotAvailable() {
                mView.showNoMatchResult();
            }
        });
    }

    @Override
    public void resultItemSelect(QueryItem item) {
        mView.showResultItemSelect(item);
        mCitiesRepository.insertCity(item, new CitiesDataSource.InsertCityCallback() {
            @Override
            public void onInsertSuccess() {
                mView.showInsertCitySuccessUi();
            }

            @Override
            public void onInsertFail() {
                mView.showInsertCityFailUi();
            }
        });
    }

}

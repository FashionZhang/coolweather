package com.ikotori.coolweather.data.source;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;

import java.util.List;

/**
 * Created by Fashion at 2018/04/20 15:31.
 * Describe:
 */

public interface CitiesDataSource {

    /*interface QueryResultCallback {

        void onQueryResultLoaded(List<QueryItem> queryItems);

        void onDataNotAvailable();
    }
*/
    interface LoadCitiesCallback {
        void onCitiesLoadSuccess(List<QueryItem> cities);

        void onDataNotAvailable();
    }

    interface InsertCityCallback {
        void onInsertSuccess();

        void onInsertFail();
    }

    interface UpdateCityCallback {
        void onSetHomeSuccess();

        void onSetHomeFail();
    }

    interface DeleteCityCallback {
        void onDeleteSuccess();

        void onDeleteFail();
    }

//    void getQueryResult(@NonNull String query, @NonNull QueryResultCallback callback);

    void getCities(@NonNull LoadCitiesCallback callback);

    void insertCity(@NonNull QueryItem city, @NonNull InsertCityCallback callback);

    void updateCity(@NonNull QueryItem city, @NonNull UpdateCityCallback callback);

    void changeHomeCity(@NonNull QueryItem city, @NonNull QueryItem originCity, @NonNull UpdateCityCallback callback);

    void deleteCity(@NonNull String cid, @NonNull DeleteCityCallback callback);
}

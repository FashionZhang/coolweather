package com.ikotori.coolweather.data.source;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;

import java.util.List;

/**
 * Created by Fashion at 2018/04/21 15:30.
 * Describe:
 */

public interface QueryDataSource {

    interface QueryResultCallback {

        void onQueryResultLoaded(List<QueryItem> queryItems);

        void onDataNotAvailable();
    }

    void getQueryResult(@NonNull String query, @NonNull QueryResultCallback callback);

}

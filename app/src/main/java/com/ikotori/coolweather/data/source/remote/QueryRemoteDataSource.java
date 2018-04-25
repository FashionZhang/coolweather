package com.ikotori.coolweather.data.source.remote;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ikotori.coolweather.api.HeWeatherApi;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.QueryDataSource;
import com.ikotori.coolweather.util.HttpUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;

/**
 * Created by Fashion at 2018/04/20 16:50.
 * Describe:
 */

public class QueryRemoteDataSource implements QueryDataSource {
    private static QueryRemoteDataSource INSTANCE;

    public static QueryRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            synchronized (QueryRemoteDataSource.class) {
                INSTANCE = new QueryRemoteDataSource();
            }
        }
        return INSTANCE;
    }

    private QueryRemoteDataSource() {

    }


    @Override
    public void getQueryResult(@NonNull String query, @NonNull QueryResultCallback callback) {
        // TODO get result from server
        QueryRemoteTask queryRemoteTask = new QueryRemoteTask(callback);
        queryRemoteTask.execute(query);
    }


    private class QueryRemoteTask extends AsyncTask<String, Object, List<QueryItem>> {

        private QueryResultCallback callback;

        public QueryRemoteTask(QueryResultCallback callback) {
            this.callback = callback;
        }

        @Override
        protected List<QueryItem> doInBackground(String... strings) {
            String query = strings[0];
            if (null == query || "".equals(query)) {
                return null;
            }
            List<QueryItem> list = new ArrayList<>();

            String url = HeWeatherApi.getInstance().buildCitySearchUrl(HeWeatherApi.CITY_SEARCH_BASE, query, null);
            try {
                Response response = HttpUtils.getInstance().getSync(url);
                if (response.isSuccessful()) {
                    JSONObject object = new JSONObject(response.body().string());
                    object = object.getJSONArray("HeWeather6").getJSONObject(0);
                    list = new Gson().fromJson(object.getString("basic"), new TypeToken<List<QueryItem>>() {
                    }.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();

            }finally {
                return list;
            }
//            list.add(new QueryItem("浦口区", "南京市", "江苏省", "中国"));
//            list.add(new QueryItem("武进区", "常州市", "江苏省", "中国"));
//            list.add(new QueryItem("浦口区", "南京市", "江苏省", "中国"));
        }

        @Override
        protected void onPostExecute(List<QueryItem> queryItems) {
            if (null != callback) {
                if (null == queryItems || queryItems.size() == 0) {
                    callback.onDataNotAvailable();
                } else {
                    callback.onQueryResultLoaded(queryItems);
                }
            }
        }
    }
}

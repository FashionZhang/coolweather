package com.ikotori.coolweather.data.source.remote;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.ikotori.coolweather.api.HeWeatherApi;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.WeatherDataSource;
import com.ikotori.coolweather.util.AppExecutors;
import com.ikotori.coolweather.util.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Fashion at 2018/04/22 12:04.
 * Describe:
 */

public class WeatherRemoteDataSource implements WeatherDataSource {
    private static WeatherRemoteDataSource INSTANCE;

    private AppExecutors mAppExecutors;

    public static WeatherRemoteDataSource getInstance(@NonNull AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (WeatherRemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WeatherRemoteDataSource(appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    private WeatherRemoteDataSource(@NonNull AppExecutors appExecutors) {
        mAppExecutors = appExecutors;
    }

    @Override
    public void loadWeatherNow(final String cid, final LoadWeatherNowCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Runnable fail = new Runnable() {
                    @Override
                    public void run() {
                        callback.loadWeatherNowFail();
                    }
                };
                HttpUtils.getInstance().getAsync(HeWeatherApi.getInstance().buildWeatherNowUrl(cid)
                        , new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                mAppExecutors.mainThread().execute(fail);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response.isSuccessful()) {
                                    try {
                                        JSONObject object = new JSONObject(response.body().string());
                                        object = object.getJSONArray(HeWeatherApi.BODY_TAG).getJSONObject(0);
                                        if (object.getString("status").equals("ok")) {
                                            WeatherNow now = new Gson().fromJson(object.getString("now"), WeatherNow.class);
                                            now.setCid(object.getJSONObject("basic").getString("cid"));
                                            now.setLocation(object.getJSONObject("basic").getString("location"));
                                            now.setLoc(object.getJSONObject("update").getString("loc"));
                                            now.setUtc(object.getJSONObject("update").getString("utc"));
                                            final WeatherNow temp = now;
                                            mAppExecutors.mainThread().execute(new Runnable() {
                                                @Override
                                                public void run() {
                                                    callback.loadWeatherNowSuccess(temp);
                                                }
                                            });
                                        } else {
                                            mAppExecutors.mainThread().execute(fail);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        mAppExecutors.mainThread().execute(fail);
                                    }
                                } else {
                                    mAppExecutors.mainThread().execute(fail);
                                }
                            }
                        });
            }
        };
        mAppExecutors.mainThread().execute(runnable);
    }

    @Override
    public void insertWeatherNow(WeatherNow now) {

    }
}

package com.ikotori.coolweather.data.source.remote;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ikotori.coolweather.api.BaiduMapWebApi;
import com.ikotori.coolweather.api.HeWeatherApi;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.CityWeather;
import com.ikotori.coolweather.data.entity.RouteStep;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.source.BaiduMapDataSource;
import com.ikotori.coolweather.util.AppExecutors;
import com.ikotori.coolweather.util.HttpUtils;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Fashion at 2018/05/09 22:22.
 * Describe:
 */

public class BaiduMapDataRemoteSource implements BaiduMapDataSource {

    private static BaiduMapDataRemoteSource INSTANCE;

    private AppExecutors mAppExecutors;

    public static BaiduMapDataRemoteSource getInstance(@NonNull AppExecutors executors) {
        if (INSTANCE == null) {
            synchronized (BaiduMapDataRemoteSource.class) {
                INSTANCE = new BaiduMapDataRemoteSource(executors);
            }
        }
        return INSTANCE;
    }

    private BaiduMapDataRemoteSource(@NonNull AppExecutors executors) {
        mAppExecutors = executors;
    }

    @Override
    public void getRoutes(@NonNull final String originLon, @NonNull final String originLat, @NonNull final String destinationLon, @NonNull final String destinationLat, @NonNull final RoutesCallback callback) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                HttpUtils.getInstance().getAsync(BaiduMapWebApi.getInstance().buildDrivingRoutesUrl(originLon, originLat, destinationLon, destinationLat), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        callback.onDataNotAvailable();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject object = new JSONObject(response.body().string());
                                if (object.getString("status").equals("0")) {
                                    String routes = object.getJSONObject("result")
                                            .getJSONArray("routes")
                                            .getJSONObject(0)
                                            .getString("steps");
                                    KLog.d(routes);
                                    callback.onRoutesLoaded(routes);
                                } else {
                                    callback.onDataNotAvailable();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                callback.onDataNotAvailable();
                            }
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };
        mAppExecutors.mainThread().execute(runnable);
    }

    @Override
    public void getLocations(@NonNull final String routes, @NonNull final RoutesToLocationsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<RouteStep> routeSteps = new Gson().fromJson(routes, new TypeToken<List<RouteStep>>() {
                }.getType());
                Iterator<RouteStep> iterator = routeSteps.iterator();
                while (iterator.hasNext()) {
                    if (iterator.next().roadType >= 4) {
                        iterator.remove();
                    }
                }
                final CountDownLatch latch = new CountDownLatch(routeSteps.size());
                final String[] cids = new String[routeSteps.size()];
                synchronized (latch) {
                    for (int i = 0; i < routeSteps.size(); i++) {
                        RouteStep.Location location = routeSteps.get(i).startLocation;
                        final int finalI = i;
                        HttpUtils.getInstance().getAsync(
                                HeWeatherApi.getInstance().buildCitySearchUrl(HeWeatherApi.CITY_SEARCH_BASE, String.format("%s,%s", location.lng, location.lat), null),
                                new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        latch.countDown();
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        try {
                                            JSONObject object = new JSONObject(response.body().string());
                                            object = object.getJSONArray(HeWeatherApi.BODY_TAG).getJSONObject(0);
                                            KLog.d(object);
                                            if (object.getString("status").equals("ok")) {
                                                JSONObject cityObject = object.getJSONArray("basic").getJSONObject(0);
                                                QueryItem city = new Gson().fromJson(cityObject.toString(), QueryItem.class);
                                                cids[finalI] = city.getCid();
                                                latch.countDown();
                                            } else {
                                                latch.countDown();
                                            }
                                        } catch (Exception e) {
                                            latch.countDown();
                                        }
                                    }
                                });
                    }
                    try {
                        latch.await(30 * 1000, TimeUnit.MILLISECONDS);
                        List<String> returnCids = new ArrayList<>(2 * cids.length / 3);
                        for (String  s : cids) {
                            if (returnCids.contains(s)) {

                            } else {
                                returnCids.add(s);
                            }
                        }
                        KLog.d(returnCids);
                        callback.onLocationsLoaded(returnCids);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        mAppExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onLoadFailed();
                            }
                        });
                    }
                }
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getCityWeathers(@NonNull final List<String> cids, @NonNull final CityWeathersCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final CityWeather[] cityWeathers = new CityWeather[cids.size()];
                final CountDownLatch latch = new CountDownLatch(cids.size());
                synchronized (latch) {
                    for (int i = 0; i < cids.size(); i++) {
                        final int finalI = i;
                        HttpUtils.getInstance().getAsync(HeWeatherApi.getInstance().buildWeatherForecast(cids.get(i)), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                latch.countDown();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    JSONObject object = new JSONObject(response.body().string());
                                    object = object.getJSONArray(HeWeatherApi.BODY_TAG).getJSONObject(0);
                                    if (object.getString("status").equals("ok")) {
                                        List<WeatherForecast> forecasts = new Gson().fromJson(object.getString("daily_forecast"), new TypeToken<List<WeatherForecast>>() {
                                        }.getType());
                                        CityWeather weather = new CityWeather();
                                        weather.city = new Gson().fromJson(object.getString("basic"), QueryItem.class);
                                        weather.forecasts = forecasts;
                                        cityWeathers[finalI] = weather;
                                        latch.countDown();
                                    } else {
                                        latch.countDown();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    latch.countDown();
                                }
                            }
                        });

                    }
                    try {
                        latch.await(15, TimeUnit.SECONDS);
                        mAppExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onCityWeathersLoaded(Arrays.asList(cityWeathers));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        mAppExecutors.mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                callback.onLoadFailed();
                            }
                        });
                    }
                }
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }


}

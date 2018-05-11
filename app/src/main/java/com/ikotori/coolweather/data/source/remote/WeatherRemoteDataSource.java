package com.ikotori.coolweather.data.source.remote;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ikotori.coolweather.api.HeWeatherApi;
import com.ikotori.coolweather.data.entity.AirNow;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherHourly;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.WeatherDataSource;
import com.ikotori.coolweather.util.AppExecutors;
import com.ikotori.coolweather.util.HttpUtils;
import com.socks.library.KLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

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
                        callback.loadWeatherNowFailed();
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
                                                    callback.loadWeatherNowSucceeded(temp);
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

    @Override
    public void loadWeatherForecasts(final String cid, final LoadWeatherForecastCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Runnable fail = new Runnable() {
                    @Override
                    public void run() {
                        callback.loadWeatherForecastFailed();
                    }
                };
                HttpUtils.getInstance().getAsync(HeWeatherApi.getInstance().buildWeatherForecast(cid), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mAppExecutors.mainThread().execute(fail);
                    }

                    @Override
                    public void onResponse(final Call call, Response response) throws IOException {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            object = object.getJSONArray(HeWeatherApi.BODY_TAG).getJSONObject(0);
                            if (object.getString("status").equals("ok")) {
                                final List<WeatherForecast> forecasts = new Gson().fromJson(object.getString("daily_forecast"), new TypeToken<List<WeatherForecast>>() {
                                }.getType());
                                String loc = object.getJSONObject("update").getString("loc");
                                for (WeatherForecast o : forecasts) {
                                    o.cid = cid;
                                    o.loc = loc;
                                }
                                mAppExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.loadWeatherForecastSucceeded(forecasts);
                                    }
                                });
                            } else {
                                mAppExecutors.mainThread().execute(fail);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mAppExecutors.mainThread().execute(fail);
                        }
                    }
                });
            }
        };
        mAppExecutors.mainThread().execute(runnable);
    }

    @Override
    public void insertWeatherForecasts(List<WeatherForecast> forecasts) {

    }

    @Override
    public void deleteWeatherForecasts(List<WeatherForecast> forecasts) {

    }

    @Override
    public void loadWeatherHourlies(final String cid, final LoadWeatherHourlyCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Runnable fail = new Runnable() {
                    @Override
                    public void run() {
                        callback.loadWeatherHourliesFailed();
                    }
                };
                HttpUtils.getInstance().getAsync(HeWeatherApi.getInstance().buildWeatherHourly(cid), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mAppExecutors.mainThread().execute(fail);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            object = object.getJSONArray(HeWeatherApi.BODY_TAG).getJSONObject(0);
                            if (object.getString("status").equals("ok")) {
                                final List<WeatherHourly> hourlies = new Gson().fromJson(object.getString("hourly"), new TypeToken<List<WeatherHourly>>() {
                                }.getType());
                                String loc = object.getJSONObject("update").getString("loc");
                                for (WeatherHourly hourly : hourlies) {
                                    hourly.setCid(cid);
                                    hourly.setLoc(loc);
                                }
                                mAppExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.loadWeatherHourliesSucceeded(hourlies);
                                    }
                                });
                            } else {
                                mAppExecutors.mainThread().execute(fail);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mAppExecutors.mainThread().execute(fail);
                        }
                    }
                });
            }
        };
        mAppExecutors.mainThread().execute(runnable);
    }

    @Override
    public void insertWeatherHourlies(List<WeatherHourly> hourlies) {

    }

    @Override
    public void deleteWeatherHourlies(List<WeatherHourly> hourlies) {

    }

    @Override
    public void loadAirNow(final String cid, final LoadAirNowCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Runnable fail = new Runnable() {
                    @Override
                    public void run() {
                        callback.loadAirNowFailed();
                    }
                };
                HttpUtils.getInstance().getAsync(HeWeatherApi.getInstance().buildAirNow(cid), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mAppExecutors.mainThread().execute(fail);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject object = new JSONObject(response.body().string());
                            object = object.getJSONArray(HeWeatherApi.BODY_TAG).getJSONObject(0);
                            KLog.d(object);
                            if (object.getString("status").equals("ok")) {
                                final AirNow now = new Gson().fromJson(object.getString("air_now_city"), AirNow.class);
                                now.cid = cid;
                                now.loc = object.getJSONObject("update").getString("loc");
                                mAppExecutors.mainThread().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.loadAirNowSucceeded(now);
                                    }
                                });
                            } else {
                                mAppExecutors.mainThread().execute(fail);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mAppExecutors.mainThread().execute(fail);
                        }
                    }
                });
            }
        };
        mAppExecutors.mainThread().execute(runnable);
    }

    @Override
    public void insertAirNow(AirNow now) {

    }

    @Override
    public void deleteAirNow(AirNow now) {

    }
}

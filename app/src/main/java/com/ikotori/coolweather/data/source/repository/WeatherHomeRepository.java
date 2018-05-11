package com.ikotori.coolweather.data.source.repository;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.AirNow;
import com.ikotori.coolweather.data.entity.Weather;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherHourly;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.WeatherDataSource;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fashion at 2018/04/22 11:08.
 * Describe:
 */

public class WeatherHomeRepository implements CitiesDataSource, WeatherDataSource {

    private static WeatherHomeRepository INSTANCE = null;

    private final CitiesDataSource mCitiesDataSource;

    private final WeatherDataSource mLocalWeatherDataSource;

    private final WeatherDataSource mRemoteWeatherDataSource;

    //内存缓存数据
    private Map<String, Weather> mCacheWeather;

    //记录缓存数据是否可用,不可用时需要从远程加载数据
    //只有当用户触发有效刷新时或者本地数据已过期才会置为true
    //以下boolean类型变量都是这种用途
    private boolean mCacheWeatherIsInvalid = false;

    private boolean mCacheWeatherNowIsInvalid = false;

    private boolean mCacheWeatherForecastsIsInvalid = false;

    private boolean mCacheWeatherHourliesIsInvalid = false;

    private boolean mCacheAirNowIsInvalid = false;

    private WeatherHomeRepository(@NonNull CitiesDataSource citiesDataSource, @NonNull WeatherDataSource local, @NonNull WeatherDataSource remote) {
        mCitiesDataSource = citiesDataSource;
        mLocalWeatherDataSource = local;
        mRemoteWeatherDataSource = remote;
    }

    public static WeatherHomeRepository getInstance(@NonNull CitiesDataSource citiesDataSource, @NonNull WeatherDataSource local, @NonNull WeatherDataSource remote) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherHomeRepository(citiesDataSource, local, remote);
        }
        return INSTANCE;
    }


    @Override
    public void getCities(@NonNull LoadCitiesCallback callback) {
        mCitiesDataSource.getCities(callback);
    }

    @Override
    public void insertCity(@NonNull QueryItem city, @NonNull InsertCityCallback callback) {

    }

    @Override
    public void updateCity(@NonNull QueryItem city, @NonNull UpdateCityCallback callback) {

    }

    @Override
    public void changeHomeCity(@NonNull QueryItem city, @NonNull QueryItem originCity, @NonNull UpdateCityCallback callback) {

    }

    @Override
    public void deleteCity(@NonNull String cid, @NonNull DeleteCityCallback callback) {

    }

    @Override
    public void loadWeatherNow(final String cid, final LoadWeatherNowCallback callback) {
        Weather weather;
        WeatherNow weatherNow;
        if (mCacheWeather != null && !mCacheWeatherNowIsInvalid
                && (weather = (Weather) mCacheWeather.get(cid)) != null
                && (weatherNow = weather.getNow()) != null) {

            callback.loadWeatherNowSucceeded(weatherNow);
            return;
        }
        if (mCacheWeatherNowIsInvalid) {
            //如果缓存被标记为不可获得的话需要从远程获取数据
            //这种情况发生在用户触发刷新时或者本地数据已过期时
            getWeatherNowFromRemote(cid, callback);
            KLog.d("远程");
        } else {
            KLog.d("本地");
            mLocalWeatherDataSource.loadWeatherNow(cid, new LoadWeatherNowCallback() {
                @Override
                public void loadWeatherNowSucceeded(WeatherNow now) {
                    refreshCache(cid, now);
                    callback.loadWeatherNowSucceeded(now);
                }

                @Override
                public void loadWeatherNowFailed() {
                    getWeatherNowFromRemote(cid, callback);
                }
            });
        }
    }

    private void getWeatherNowFromRemote(@NonNull final String cid, @NonNull final LoadWeatherNowCallback callback) {
        KLog.d("远程");
        mRemoteWeatherDataSource.loadWeatherNow(cid, new LoadWeatherNowCallback() {

            @Override
            public void loadWeatherNowSucceeded(WeatherNow now) {
                refreshCacheAndLocalDataSource(cid, now);
                callback.loadWeatherNowSucceeded(now);
            }

            @Override
            public void loadWeatherNowFailed() {
                callback.loadWeatherNowFailed();
            }
        });
    }

    @Override
    public void insertWeatherNow(WeatherNow now) {
        mLocalWeatherDataSource.insertWeatherNow(now);
    }

    @Override
    public void loadWeatherForecasts(@NonNull final String cid, @NonNull final LoadWeatherForecastCallback callback) {
        Weather weather;
        List<WeatherForecast> weatherForecasts;
        if (mCacheWeather != null && !mCacheWeatherForecastsIsInvalid
                && (weather = mCacheWeather.get(cid)) != null
                && (weatherForecasts = weather.getWeatherForecasts()) != null) {
            callback.loadWeatherForecastSucceeded(weatherForecasts);
            return;
        }
        if (mCacheWeatherForecastsIsInvalid) {
            getWeatherForecastsRemote(cid, callback);
        }
        else {
            mLocalWeatherDataSource.loadWeatherForecasts(cid, new LoadWeatherForecastCallback() {
                @Override
                public void loadWeatherForecastSucceeded(List<WeatherForecast> forecasts) {
                    refreshCache(cid, forecasts);
                    callback.loadWeatherForecastSucceeded(forecasts);
                }

                @Override
                public void loadWeatherForecastFailed() {
                    getWeatherForecastsRemote(cid, callback);
                }
            });
        }
    }

    private void getWeatherForecastsRemote(final String cid, final LoadWeatherForecastCallback callback) {
        KLog.d();
        mRemoteWeatherDataSource.loadWeatherForecasts(cid, new LoadWeatherForecastCallback() {
            @Override
            public void loadWeatherForecastSucceeded(List<WeatherForecast> forecasts) {
                refreshCacheAndLocalDataSource(cid, forecasts);
                callback.loadWeatherForecastSucceeded(forecasts);
            }

            @Override
            public void loadWeatherForecastFailed() {
                callback.loadWeatherForecastFailed();
            }
        });
    }

    @Override
    public void insertWeatherForecasts(List<WeatherForecast> forecasts) {

    }

    @Override
    public void deleteWeatherForecasts(List<WeatherForecast> forecasts) {

    }

    @Override
    public void loadWeatherHourlies(final String cid, final LoadWeatherHourlyCallback callback) {
        Weather weather;
        List<WeatherHourly> weatherHourlies;
        if (mCacheWeather != null && !mCacheWeatherHourliesIsInvalid
                && (weather = mCacheWeather.get(cid)) != null
                && (weatherHourlies = weather.getWeatherHourlies()) != null) {
            callback.loadWeatherHourliesSucceeded(weatherHourlies);
            return;
        }
        if (mCacheWeatherHourliesIsInvalid) {
            getWeatherHourliesRemote(cid, callback);
        } else {
            mLocalWeatherDataSource.loadWeatherHourlies(cid, new LoadWeatherHourlyCallback() {
                @Override
                public void loadWeatherHourliesSucceeded(List<WeatherHourly> hourlies) {
                    refreshCache(cid, hourlies);
                    callback.loadWeatherHourliesSucceeded(hourlies);
                }

                @Override
                public void loadWeatherHourliesFailed() {
                    getWeatherHourliesRemote(cid, callback);
                }
            });
        }
    }

    private void getWeatherHourliesRemote(final String cid, final LoadWeatherHourlyCallback callback) {
        mRemoteWeatherDataSource.loadWeatherHourlies(cid, new LoadWeatherHourlyCallback() {
            @Override
            public void loadWeatherHourliesSucceeded(List<WeatherHourly> hourlies) {
                refreshCacheAndLocalDataSource(cid, hourlies);
                callback.loadWeatherHourliesSucceeded(hourlies);
            }

            @Override
            public void loadWeatherHourliesFailed() {
                callback.loadWeatherHourliesFailed();
            }
        });
    }

    @Override
    public void insertWeatherHourlies(List<WeatherHourly> hourlies) {

    }

    @Override
    public void deleteWeatherHourlies(List<WeatherHourly> hourlies) {

    }

    @Override
    public void loadAirNow(final String cid, final LoadAirNowCallback callback) {
        Weather weather;
        AirNow airNow;
        if (mCacheWeather != null && !mCacheAirNowIsInvalid
                && (weather = mCacheWeather.get(cid)) != null
                && (airNow = weather.getAirNow()) != null) {
            callback.loadAirNowSucceeded(airNow);
            return;
        }
        if (mCacheAirNowIsInvalid) {
            getAirNowRemote(cid, callback);
        } else {
            mLocalWeatherDataSource.loadAirNow(cid, new LoadAirNowCallback() {
                @Override
                public void loadAirNowSucceeded(AirNow now) {
                    refreshCache(cid, now);
                    callback.loadAirNowSucceeded(now);
                }

                @Override
                public void loadAirNowFailed() {
                    getAirNowRemote(cid, callback);
                }
            });
        }
    }

    private void getAirNowRemote(final String cid, final LoadAirNowCallback callback) {
        mRemoteWeatherDataSource.loadAirNow(cid, new LoadAirNowCallback() {
            @Override
            public void loadAirNowSucceeded(AirNow now) {
                refreshCacheAndLocalDataSource(cid, now);
                callback.loadAirNowSucceeded(now);
            }

            @Override
            public void loadAirNowFailed() {
                callback.loadAirNowFailed();
            }
        });
    }

    @Override
    public void insertAirNow(AirNow now) {

    }

    @Override
    public void deleteAirNow(AirNow now) {

    }


    private void refreshCacheAndLocalDataSource(@NonNull final String cid, final Object... objects) {
        Object o = objects[0];
        if (mCacheWeather == null) {
            mCacheWeather = new HashMap<>();
        }
        Weather weather = new Weather();
        if (mCacheWeather.containsKey(cid)) {
            weather = mCacheWeather.get(cid);
        }
        if (o instanceof WeatherNow) {
            weather.setNow((WeatherNow) o);
            mCacheWeatherNowIsInvalid = false;
            mLocalWeatherDataSource.insertWeatherNow((WeatherNow) o);
        } else if (o instanceof WeatherForecast) {
            List<WeatherForecast> forecasts = new ArrayList<>();
            for (Object obj : objects) {
                forecasts.add((WeatherForecast) obj);
            }
            if (weather.getWeatherForecasts() != null && weather.getWeatherForecasts().size() > 0) {
                mLocalWeatherDataSource.deleteWeatherForecasts(weather.getWeatherForecasts());
            }
            weather.setWeatherForecasts(forecasts);
            mCacheWeatherForecastsIsInvalid = false;
            mLocalWeatherDataSource.insertWeatherForecasts(forecasts);
        } else if (o instanceof WeatherHourly) {
            List<WeatherHourly> hourlies = new ArrayList<>();
            for (Object obj : objects) {
                hourlies.add((WeatherHourly) obj);
            }
            if (weather.getWeatherHourlies() != null && weather.getWeatherHourlies().size() > 0) {
                mLocalWeatherDataSource.deleteWeatherHourlies(weather.getWeatherHourlies());
            }
            weather.setWeatherHourlies(hourlies);
            mCacheWeatherHourliesIsInvalid = false;
            mLocalWeatherDataSource.insertWeatherHourlies(hourlies);
        } else if (o instanceof AirNow) {
            weather.setAirNow((AirNow) o);
            mCacheAirNowIsInvalid = false;
            mLocalWeatherDataSource.insertAirNow((AirNow) o);
        }
        mCacheWeather.put(cid, weather);
    }

    private void refreshCache(@NonNull final String cid, final Object... objects) {
        Object o = objects[0];
        if (mCacheWeather == null) {
            mCacheWeather = new HashMap<>();
        }
        Weather weather = new Weather();
        if (mCacheWeather.containsKey(cid)) {
            weather = mCacheWeather.get(cid);
        }
        if (o instanceof WeatherNow) {
            weather.setNow((WeatherNow) o);
            mCacheWeatherNowIsInvalid = false;
        } else if (o instanceof WeatherForecast) {
            List<WeatherForecast> forecasts = new ArrayList<>();
            for (Object obj : objects) {
                forecasts.add((WeatherForecast) obj);
            }
            weather.setWeatherForecasts(forecasts);
            mCacheWeatherForecastsIsInvalid = false;
        } else if (o instanceof WeatherHourly) {
            List<WeatherHourly> hourlies = new ArrayList<>();
            for (Object obj : objects) {
                hourlies.add((WeatherHourly) obj);
            }
            weather.setWeatherHourlies(hourlies);
            mCacheWeatherHourliesIsInvalid = false;
        } else if (o instanceof AirNow) {
            weather.setAirNow((AirNow) o);
            mCacheAirNowIsInvalid = false;
        }
        mCacheWeather.put(cid, weather);
    }

}

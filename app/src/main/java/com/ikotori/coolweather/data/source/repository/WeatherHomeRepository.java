package com.ikotori.coolweather.data.source.repository;

import android.support.annotation.NonNull;

import com.baidu.location.BDLocation;
import com.ikotori.coolweather.data.BaiduLocationDataSource;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.AirNow;
import com.ikotori.coolweather.data.entity.Weather;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherHourly;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.QueryDataSource;
import com.ikotori.coolweather.data.source.WeatherDataSource;
import com.ikotori.coolweather.util.DateUtil;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Fashion at 2018/04/22 11:08.
 * Describe:
 */

public class WeatherHomeRepository implements CitiesDataSource, WeatherDataSource, QueryDataSource {

    private static WeatherHomeRepository INSTANCE = null;

    private final CitiesDataSource mCitiesDataSource;

    private final WeatherDataSource mLocalWeatherDataSource;

    private final WeatherDataSource mRemoteWeatherDataSource;

    private final BaiduLocationDataSource mLocationDataSource;

    private final QueryDataSource mRemoteCityDataSource;

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

    private BDLocation mLocation;

    private WeatherHomeRepository(@NonNull CitiesDataSource citiesDataSource, @NonNull WeatherDataSource local, @NonNull WeatherDataSource remote, @NonNull BaiduLocationDataSource locationDataSource, QueryDataSource remoteCityDataSource) {
        mCitiesDataSource = citiesDataSource;
        mLocalWeatherDataSource = local;
        mRemoteWeatherDataSource = remote;
        mLocationDataSource = locationDataSource;
        mRemoteCityDataSource = remoteCityDataSource;
    }

    public static WeatherHomeRepository getInstance(@NonNull CitiesDataSource citiesDataSource, @NonNull WeatherDataSource local, @NonNull WeatherDataSource remote, @NonNull BaiduLocationDataSource locationDataSource, @NonNull QueryDataSource remoteCityDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new WeatherHomeRepository(citiesDataSource, local, remote, locationDataSource, remoteCityDataSource);
        }
        return INSTANCE;
    }


    @Override
    public void getCities(@NonNull LoadCitiesCallback callback) {
        mCitiesDataSource.getCities(callback);
    }

    @Override
    public void insertCity(@NonNull QueryItem city, @NonNull InsertCityCallback callback) {
        mCitiesDataSource.insertCity(city, callback);
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
        } else {
            KLog.d("从本地加载实时天气数据");
            mLocalWeatherDataSource.loadWeatherNow(cid, new LoadWeatherNowCallback() {
                @Override
                public void loadWeatherNowSucceeded(WeatherNow now) {
                    refreshCache(cid, now);
                    if (whetherRefresh(now, WeatherNow.class)) {
                        getWeatherNowFromRemote(cid, callback);
                    } else {
                        callback.loadWeatherNowSucceeded(now);
                    }
                }

                @Override
                public void loadWeatherNowFailed() {
                    getWeatherNowFromRemote(cid, callback);
                }
            });
        }
    }

    private void getWeatherNowFromRemote(@NonNull final String cid, @NonNull final LoadWeatherNowCallback callback) {
        KLog.d("从服务器加载实时天气数据");
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
            KLog.d("从本地加载未来天气数据");
            mLocalWeatherDataSource.loadWeatherForecasts(cid, new LoadWeatherForecastCallback() {
                @Override
                public void loadWeatherForecastSucceeded(List<WeatherForecast> forecasts) {
                    refreshCache(cid, forecasts);
                    if (forecasts.size() > 0) {
                        if (whetherRefresh(forecasts.get(0), WeatherForecast.class)) {
                            getWeatherForecastsRemote(cid, callback);
                            return;
                        }
                    }
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
        KLog.d("从服务器加载未来天气数据");
        mRemoteWeatherDataSource.loadWeatherForecasts(cid, new LoadWeatherForecastCallback() {
            @Override
            public void loadWeatherForecastSucceeded(List<WeatherForecast> forecasts) {
                refreshCacheAndLocalDataSource(cid, (Object[]) forecasts.toArray(new WeatherForecast[forecasts.size()]));
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
                refreshCacheAndLocalDataSource(cid, (Object[]) hourlies.toArray(new WeatherHourly[hourlies.size()]));
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
            KLog.d("从本地加载空气质量实况数据");
            mLocalWeatherDataSource.loadAirNow(cid, new LoadAirNowCallback() {
                @Override
                public void loadAirNowSucceeded(AirNow now) {
                    refreshCache(cid, now);
                    if (whetherRefresh(now, AirNow.class)) {
                        getAirNowRemote(cid, callback);
                        return;
                    }
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
        KLog.d("从服务器加载空气质量实况数据");
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


    public void forceUpdate(String cid) {
        if (mCacheWeather.get(cid) == null) {
            mCacheWeatherIsInvalid = true;
            return;
        }
        Weather weather = mCacheWeather.get(cid);
        mCacheWeatherNowIsInvalid = whetherRefresh(weather.getNow(), WeatherNow.class);
        if (weather.getWeatherForecasts() != null && weather.getWeatherForecasts().size() > 0) {
            mCacheWeatherForecastsIsInvalid = whetherRefresh(weather.getWeatherForecasts().get(0), WeatherForecast.class);
        }
//        mCacheWeatherHourliesIsInvalid = true;
        mCacheAirNowIsInvalid = whetherRefresh(weather.getAirNow(), AirNow.class);
    }

    public void getLocation(boolean update, final BaiduLocationDataSource.LocationCallback callback) {
        if (update) {
            mLocation = null;
        }
        if (mLocation == null) {
            mLocationDataSource.start(new BaiduLocationDataSource.LocationCallback() {
                @Override
                public void fail() {
                    callback.fail();
                }

                @Override
                public void success(BDLocation location) {
                    mLocation = location;
                    callback.success(location);
                }
            });
        } else {
            callback.success(mLocation);
        }
    }

    public void stopLocationService() {
        mLocationDataSource.stop();
    }

    @Override
    public void getQueryResult(@NonNull String query, @NonNull QueryResultCallback callback) {
        mRemoteCityDataSource.getQueryResult(query, callback);
    }

    /**
     * 判断是否需要从服务器获取最新数据
     * @param o
     * @param clazz
     * @return
     */
    private boolean whetherRefresh(Object o, Class<?> clazz) {
        if (o == null) {
            if (clazz == WeatherNow.class) {
                mCacheWeatherNowIsInvalid = true;
            } else if (clazz == WeatherForecast.class) {
                mCacheWeatherForecastsIsInvalid = true;
            } else if (clazz == WeatherHourly.class) {
                mCacheWeatherHourliesIsInvalid = true;
            } else if (clazz == AirNow.class) {
                mCacheAirNowIsInvalid = true;
            }
            return true;
        } else {
            if (clazz == WeatherNow.class) {
                if (DateUtil.secondsFromNow(((WeatherNow) o).getLoc()) > 30 * 60) {
                    mCacheWeatherNowIsInvalid = true;
                    return true;
                }
            } else if (clazz == WeatherForecast.class) {
                if (DateUtil.secondsFromNow(((WeatherForecast) o).loc) > 60 * 60) {
                    mCacheWeatherForecastsIsInvalid = true;
                    return true;
                }
            } else if (clazz == AirNow.class) {
                if (DateUtil.secondsFromNow(((AirNow) o).loc) > 45 * 60) {
                    mCacheAirNowIsInvalid = true;
                    return true;
                }
            }
        }
        return false;
    }
}

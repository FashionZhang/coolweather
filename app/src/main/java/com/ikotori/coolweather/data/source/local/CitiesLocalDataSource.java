package com.ikotori.coolweather.data.source.local;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.util.AppExecutors;
import com.socks.library.KLog;

import java.util.List;

/**
 * Created by Fashion at 2018/04/21 15:47.
 * Describe:
 */

public class CitiesLocalDataSource implements CitiesDataSource {

    private static volatile CitiesLocalDataSource INSTANCE;

    private CitiesDao mCitiesDao;

    private AppExecutors mAppExecutors;

    private CitiesLocalDataSource(@NonNull CitiesDao dao, AppExecutors appExecutors) {
        mCitiesDao = dao;
        mAppExecutors = appExecutors;
    }

    public static CitiesLocalDataSource getInstance(@NonNull CitiesDao dao, AppExecutors appExecutors) {
        if (INSTANCE == null) {
            synchronized (CitiesLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CitiesLocalDataSource(dao, appExecutors);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getCities(@NonNull final LoadCitiesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<QueryItem> cities = mCitiesDao.getCities();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (cities.isEmpty()) {
                            callback.onDataNotAvailable();
                        } else {
                            callback.onCitiesLoadSuccess(cities);
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void insertCity(@NonNull final QueryItem city, @NonNull final InsertCityCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                boolean flag = false;
                if (mCitiesDao.countCities() == 0) {
                    city.setHome(true);
                }
                if (mCitiesDao.getCityById(city.getCid()) != null) {
                    flag = true;
                } else {
                    mCitiesDao.insertCity(city);
                }
                final boolean exist = flag;
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (!exist) {
                            callback.onInsertSuccess();
                        } else {
                            callback.onInsertFail();
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void updateCity(@NonNull final QueryItem city, @NonNull final UpdateCityCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final int rows = mCitiesDao.updateCity(city);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (rows < 1) {
                            callback.onSetHomeFail();
                        } else {
                            callback.onSetHomeSuccess();
                        }

                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void changeHomeCity(@NonNull final QueryItem city, @NonNull final QueryItem originCity, @NonNull final UpdateCityCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mCitiesDao.updateCity(city);
                mCitiesDao.updateCity(originCity);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSetHomeSuccess();
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void deleteCity(@NonNull final String cid, @NonNull final DeleteCityCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final int rows = mCitiesDao.deleteTaskById(cid);
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (rows < 1) {
                            callback.onDeleteFail();
                        } else {
                            callback.onDeleteSuccess();
                        }
                    }
                });
            }
        };
        mAppExecutors.diskIO().execute(runnable);
    }
}

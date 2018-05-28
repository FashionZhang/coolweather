package com.ikotori.coolweather.home;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.baidu.location.BDLocation;
import com.ikotori.coolweather.data.BaiduLocationDataSource;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.QueryDataSource;
import com.ikotori.coolweather.data.source.repository.WeatherHomeRepository;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fashion at 2018/04/18 22:09.
 * Describe:
 */

public class WeatherHomePresenter implements WeatherHomeContract.Presenter {

    private final WeatherHomeContract.View mHomeView;

    private final WeatherHomeRepository mHomeRepository;

    public WeatherHomePresenter(@NonNull WeatherHomeContract.View view, @NonNull WeatherHomeRepository repository) {
        mHomeView = view;
        mHomeView.setPresenter(this);
        mHomeRepository = repository;
    }

    @Override
    public void start() {
        loadAllCities();
    }

    @Override
    public void openCitySelect() {
        mHomeView.showCitySelectUi();
    }

    @Override
    public void openSetting() {
        mHomeView.showSettingUi();
    }

    @Override
    public void openShare() {
        mHomeView.showShareUi();
    }

    @Override
    public void loadAllCities() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final QueryItem[] locationCity = new QueryItem[1];
                final List<QueryItem> userCities = new ArrayList<>();
                final CountDownLatch latch = new CountDownLatch(2);
                Thread location = new Thread() {
                    @Override
                    public void run() {
                        mHomeRepository.getLocation(false, new BaiduLocationDataSource.LocationCallback() {
                            @Override
                            public void fail() {
                                latch.countDown();
                            }

                            @Override
                            public void success(BDLocation location) {
                                KLog.d(location.getAddress() + location.getCity() + location.getDistrict() + "," + location.getLongitude() + ", " + location.getLatitude());
                                mHomeRepository.getQueryResult(String.format("%s,%s", location.getLongitude(), location.getLatitude()), new QueryDataSource.QueryResultCallback() {
                                    @Override
                                    public void onQueryResultLoaded(List<QueryItem> queryItems) {
                                        KLog.d(queryItems);
                                        locationCity[0] = queryItems.get(0);
                                        latch.countDown();
                                    }

                                    @Override
                                    public void onDataNotAvailable() {
                                        latch.countDown();
                                    }
                                });
                            }
                        });
                    }
                };
                Thread city = new Thread() {
                    @Override
                    public void run() {
                        mHomeRepository.getCities(new CitiesDataSource.LoadCitiesCallback() {
                            @Override
                            public void onCitiesLoadSuccess(List<QueryItem> cities) {
                                for (int i = 0; i < cities.size(); i++) {
                                    if (cities.get(i).isHome() && i > 0) {
                                        QueryItem o = cities.get(i);
                                        cities.remove(i);
                                        cities.add(0, o);
                                        break;
                                    }
                                }
                                userCities.addAll(cities);
                                latch.countDown();
                            }

                            @Override
                            public void onDataNotAvailable() {
                                latch.countDown();
                            }
                        });
                    }
                };
                location.start();
                city.start();
                try {
                    latch.await();
                    mHomeRepository.stopLocationService();
                    Handler mainThreadHandler = new Handler(Looper.getMainLooper());
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            if (locationCity[0] == null) {
                                if (userCities.size() > 0) {
                                    mHomeView.allCitiesLoaded(userCities);
                                } else {
                                    mHomeView.showNoCityUi();
                                }
                            } else {
                                QueryItem item = locationCity[0];
                                if (isContain(item, userCities)) {
                                    mHomeView.allCitiesLoaded(userCities);
                                } else {
                                    userCities.add(item);
                                    mHomeView.allCitiesLoaded(userCities);
                                    mHomeRepository.insertCity(item, new CitiesDataSource.InsertCityCallback() {
                                        @Override
                                        public void onInsertSuccess() {

                                        }

                                        @Override
                                        public void onInsertFail() {

                                        }
                                    });
                                }
                            }
                        }
                    };
                    mainThreadHandler.post(runnable);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void stopLocationService() {
        mHomeRepository.stopLocationService();
    }

    /**
     * 以cid作为标准判断城市是否存在.
     *
     * @param city
     * @param cities
     * @return
     */
    private boolean isContain(QueryItem city, List<QueryItem> cities) {
        for (QueryItem o : cities) {
            if (o.getCid().equals(city.getCid())) {
                return true;
            }
        }
        return false;
    }
}

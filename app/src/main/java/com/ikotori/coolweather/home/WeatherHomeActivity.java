package com.ikotori.coolweather.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.customview.NavigationViewPager;
import com.ikotori.coolweather.data.source.local.CitiesLocalDataSource;
import com.ikotori.coolweather.data.source.local.CoolWeatherDatabase;
import com.ikotori.coolweather.data.source.local.WeatherLocalDataSource;
import com.ikotori.coolweather.data.source.remote.QueryRemoteDataSource;
import com.ikotori.coolweather.data.source.remote.WeatherRemoteDataSource;
import com.ikotori.coolweather.data.source.repository.QuickWeatherRepository;
import com.ikotori.coolweather.data.source.repository.WeatherHomeRepository;
import com.ikotori.coolweather.home.quickweather.QuickWeatherFragment;
import com.ikotori.coolweather.home.quickweather.QuickWeatherPresenter;
import com.ikotori.coolweather.home.travelweather.TravelWeatherFragment;
import com.ikotori.coolweather.home.travelweather.TravelWeatherPresenter;
import com.ikotori.coolweather.util.AppExecutors;

public class WeatherHomeActivity extends AppCompatActivity {

    private WeatherHomePresenter mWeatherPresenter;

    private QuickWeatherPresenter mQuickPresenter;

    private TravelWeatherPresenter mTravelPresenter;

    private static final int WEATHER_HOME = 0;

    private static final int QUICK_WEATHER = 1;

    private static final int TRAVEL_WEATHER = 2;


    private WeatherHomeFragment weatherHomeFragment;
    private QuickWeatherFragment quickWeatherFragment;
    private TravelWeatherFragment travelWeatherFragment;

    private NavigationViewPager mNavigationViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);
        CoolWeatherDatabase database = CoolWeatherDatabase.getInstance(getApplicationContext());
        weatherHomeFragment = WeatherHomeFragment.newInstance();
        quickWeatherFragment = QuickWeatherFragment.newInstance();
        travelWeatherFragment = TravelWeatherFragment.newInstance();
        mWeatherPresenter = new WeatherHomePresenter(weatherHomeFragment, WeatherHomeRepository.getInstance(CitiesLocalDataSource.getInstance(database.citiesDao(), new AppExecutors()),
                WeatherLocalDataSource.getInstance(new AppExecutors(), database.weatherDao()), WeatherRemoteDataSource.getInstance(new AppExecutors())));
        mQuickPresenter = new QuickWeatherPresenter(quickWeatherFragment, QuickWeatherRepository.getInstance(QueryRemoteDataSource.getInstance(),WeatherRemoteDataSource.getInstance(new AppExecutors())));
        mTravelPresenter = new TravelWeatherPresenter(travelWeatherFragment);

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initViewPager();
    }

    private void initViewPager() {
        if (mNavigationViewPager == null) {
            mNavigationViewPager = (NavigationViewPager)findViewById(R.id.navigation_viewpager);
        }
        NavigationAdapter navigationAdapter = new NavigationAdapter(getSupportFragmentManager());
        navigationAdapter.addFragmnet(weatherHomeFragment);
        navigationAdapter.addFragmnet(quickWeatherFragment);
        navigationAdapter.addFragmnet(travelWeatherFragment);
        mNavigationViewPager.setAdapter(navigationAdapter);
        mNavigationViewPager.setOffscreenPageLimit(2);
        mNavigationViewPager.setCurrentItem(0);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.weather_home:
                    mNavigationViewPager.setCurrentItem(WEATHER_HOME);
                    return true;
                case R.id.quick_weather:
                    mNavigationViewPager.setCurrentItem(QUICK_WEATHER);
                    return true;
                case R.id.travel_weather:
                    mNavigationViewPager.setCurrentItem(TRAVEL_WEATHER);
                    return true;
            }
            return false;
        }
    };

}

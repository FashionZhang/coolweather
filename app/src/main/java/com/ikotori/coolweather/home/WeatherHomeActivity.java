package com.ikotori.coolweather.home;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.ikotori.coolweather.GlobalApplication;
import com.ikotori.coolweather.R;
import com.ikotori.coolweather.customview.NavigationViewPager;
import com.ikotori.coolweather.data.BaiduLocationDataSource;
import com.ikotori.coolweather.data.source.local.CitiesLocalDataSource;
import com.ikotori.coolweather.data.source.local.CoolWeatherDatabase;
import com.ikotori.coolweather.data.source.local.WeatherLocalDataSource;
import com.ikotori.coolweather.data.source.remote.BaiduMapDataRemoteSource;
import com.ikotori.coolweather.data.source.remote.QueryRemoteDataSource;
import com.ikotori.coolweather.data.source.remote.WeatherRemoteDataSource;
import com.ikotori.coolweather.data.source.repository.QuickWeatherRepository;
import com.ikotori.coolweather.data.source.repository.TravelWeatherRepository;
import com.ikotori.coolweather.data.source.repository.WeatherHomeRepository;
import com.ikotori.coolweather.home.quickweather.QuickWeatherFragment;
import com.ikotori.coolweather.home.quickweather.QuickWeatherPresenter;
import com.ikotori.coolweather.home.travelweather.TravelWeatherFragment;
import com.ikotori.coolweather.home.travelweather.TravelWeatherPresenter;
import com.ikotori.coolweather.util.AppExecutors;

import java.util.ArrayList;

public class WeatherHomeActivity extends AppCompatActivity {

    private WeatherHomePresenter mWeatherPresenter;

    private QuickWeatherPresenter mQuickPresenter;

    private TravelWeatherPresenter mTravelPresenter;

    private static final int WEATHER_HOME = 0;

    private static final int QUICK_WEATHER = 1;

    private static final int TRAVEL_WEATHER = 2;

    private final int SDK_PERMISSION_REQUEST = 127;

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
                WeatherLocalDataSource.getInstance(new AppExecutors(), database.weatherDao()), WeatherRemoteDataSource.getInstance(new AppExecutors()),
                BaiduLocationDataSource.getInstance(((GlobalApplication)getApplication()).mLocationService),
                QueryRemoteDataSource.getInstance()));
        mQuickPresenter = new QuickWeatherPresenter(quickWeatherFragment, QuickWeatherRepository.getInstance(QueryRemoteDataSource.getInstance(),WeatherRemoteDataSource.getInstance(new AppExecutors())));
        mTravelPresenter = new TravelWeatherPresenter(travelWeatherFragment, TravelWeatherRepository.getInstance(BaiduMapDataRemoteSource.getInstance(new AppExecutors()), QueryRemoteDataSource.getInstance()));

        BottomNavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        initViewPager();
        getPermissions();

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

    /**
     * 申请定位权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void getPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<>();
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }

            //精确定位
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}

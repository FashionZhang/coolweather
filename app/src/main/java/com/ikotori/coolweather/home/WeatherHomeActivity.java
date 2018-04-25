package com.ikotori.coolweather.home;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.source.CitiesDataSource;
import com.ikotori.coolweather.data.source.local.CitiesLocalDataSource;
import com.ikotori.coolweather.data.source.local.CoolWeatherDatabase;
import com.ikotori.coolweather.data.source.local.WeatherLocalDataSource;
import com.ikotori.coolweather.data.source.remote.QueryRemoteDataSource;
import com.ikotori.coolweather.data.source.remote.WeatherRemoteDataSource;
import com.ikotori.coolweather.data.source.repository.WeatherHomeRepository;
import com.ikotori.coolweather.util.ActivityUtils;
import com.ikotori.coolweather.util.AppExecutors;

public class WeatherHomeActivity extends AppCompatActivity {

    private WeatherHomePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        WeatherHomeFragment fragment =
                (WeatherHomeFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == fragment) {
            fragment = WeatherHomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    fragment, R.id.contentFrame);
        }
        CoolWeatherDatabase database = CoolWeatherDatabase.getInstance(getApplicationContext());
        mPresenter = new WeatherHomePresenter(fragment, WeatherHomeRepository.getInstance(CitiesLocalDataSource.getInstance(database.citiesDao(), new AppExecutors()),
                WeatherLocalDataSource.getInstance(new AppExecutors()), WeatherRemoteDataSource.getInstance(new AppExecutors())));
    }

}

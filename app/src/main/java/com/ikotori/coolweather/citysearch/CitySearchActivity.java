package com.ikotori.coolweather.citysearch;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.source.local.CitiesLocalDataSource;
import com.ikotori.coolweather.data.source.local.CoolWeatherDatabase;
import com.ikotori.coolweather.data.source.remote.QueryRemoteDataSource;
import com.ikotori.coolweather.data.source.repository.CitiesRepository;
import com.ikotori.coolweather.util.ActivityUtils;
import com.ikotori.coolweather.util.AppExecutors;


public class CitySearchActivity extends AppCompatActivity {


    private CitySearchPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        CitySearchFragment fragment = (CitySearchFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (null == fragment) {
            fragment = CitySearchFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }
        CoolWeatherDatabase database = CoolWeatherDatabase.getInstance(getApplicationContext());
        mPresenter = new CitySearchPresenter(fragment, CitiesRepository.getInstance(
                QueryRemoteDataSource.getInstance(), CitiesLocalDataSource.getInstance(
                        database.citiesDao(), new AppExecutors())
        ));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

package com.ikotori.coolweather.cityselect;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.source.local.CitiesLocalDataSource;
import com.ikotori.coolweather.data.source.local.CoolWeatherDatabase;
import com.ikotori.coolweather.data.source.remote.QueryRemoteDataSource;
import com.ikotori.coolweather.data.source.repository.CitiesRepository;
import com.ikotori.coolweather.util.ActivityUtils;
import com.ikotori.coolweather.util.AppExecutors;
import com.socks.library.KLog;

public class CitySelectActivity extends AppCompatActivity {
    public boolean mAddDelete = false;

    private CitySelectPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.city_manage);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        CitySelectFragment fragment = (CitySelectFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (null == fragment) {
            fragment = CitySelectFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        CoolWeatherDatabase database = CoolWeatherDatabase.getInstance(getApplicationContext());
        mPresenter = new CitySelectPresenter(fragment, CitiesRepository.getInstance(
                QueryRemoteDataSource.getInstance(), CitiesLocalDataSource.getInstance(database.citiesDao(), new AppExecutors())
        ));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(CitySelectFragment.ADD_DELETE, mAddDelete);
        KLog.d(">>>:" + mAddDelete);
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }
}

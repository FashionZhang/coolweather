package com.ikotori.coolweather.home;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ikotori.coolweather.GlobalApplication;
import com.ikotori.coolweather.R;

import com.ikotori.coolweather.cityselect.CitySelectActivity;
import com.ikotori.coolweather.cityselect.CitySelectFragment;
import com.ikotori.coolweather.data.BaiduLocationDataSource;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.data.source.local.CitiesLocalDataSource;
import com.ikotori.coolweather.data.source.local.CoolWeatherDatabase;
import com.ikotori.coolweather.data.source.local.WeatherLocalDataSource;
import com.ikotori.coolweather.data.source.remote.QueryRemoteDataSource;
import com.ikotori.coolweather.data.source.remote.WeatherRemoteDataSource;
import com.ikotori.coolweather.data.source.repository.WeatherHomeRepository;
import com.ikotori.coolweather.home.weather.WeatherPagerAdapterPresenter;
import com.ikotori.coolweather.home.weather.WeatherFragment;
import com.ikotori.coolweather.setting.SettingsActivity;
import com.ikotori.coolweather.util.AppExecutors;
import com.ikotori.coolweather.util.DateUtil;
import com.ikotori.coolweather.util.FileUtils;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherHomeFragment extends Fragment implements WeatherHomeContract.View, WeatherFragment.ToolBarTitleListener {

    private ViewPager mWeatherPager;

    private WeatherHomeContract.Presenter mPresenter;

    // adapter实现了presenter接口,也是一个presenter
    private WeatherPagerAdapterPresenter<WeatherFragment> mPageAdapter;

    private View mNoCityView;

    private Toolbar mToolbar;

    private ActionBar mActionBar;

    private String mCityCid;

    private List<QueryItem> mCities = new ArrayList<>();
    public WeatherHomeFragment() {
        // Required empty public constructor
    }

    public static WeatherHomeFragment newInstance() {
        return new WeatherHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mPresenter.start();
    }

    @Override
    public void onResume() {
        mToolbar.getMenu().clear();
        mToolbar.inflateMenu(R.menu.home_menu);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        KLog.e();
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_weather_home, container, false);
        // TODO 在这bind view

        mNoCityView = root.findViewById(R.id.no_city);
        mWeatherPager = root.findViewById(R.id.weather_viewpager);
        CoolWeatherDatabase database = CoolWeatherDatabase.getInstance(getActivity().getApplicationContext());
        mPageAdapter = new WeatherPagerAdapterPresenter(getChildFragmentManager(),
                WeatherHomeRepository.getInstance(CitiesLocalDataSource.getInstance(database.citiesDao(), new AppExecutors()),
                        WeatherLocalDataSource.getInstance(new AppExecutors(), database.weatherDao()),
                        WeatherRemoteDataSource.getInstance(new AppExecutors()),
                        BaiduLocationDataSource.getInstance(((GlobalApplication) getActivity().getApplication()).mLocationService),
                        QueryRemoteDataSource.getInstance()
                ));
        mWeatherPager.setAdapter(mPageAdapter);
        mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((WeatherHomeActivity) getActivity()).setSupportActionBar(mToolbar);
        mActionBar = ((WeatherHomeActivity) getActivity()).getSupportActionBar();
        setHasOptionsMenu(true);
        mPresenter.start();
        return root;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.location_city:
                mPresenter.openCitySelect();
                break;
            case R.id.setting:
                mPresenter.openSetting();
                break;
            case R.id.share:
                mPresenter.openShare();
                break;
            case android.R.id.home:
                break;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean shouldRestart = data.getBooleanExtra(CitySelectFragment.ADD_DELETE, false);
        if (resultCode == Activity.RESULT_OK) {
            mCityCid = data.getStringExtra(CitySelectFragment.PAGE_INDEX);
            if (!shouldRestart) {
                reselectItem();
            }
        }
        if (shouldRestart) {
            mPresenter.start();
        }
    }

    private void reselectItem() {
        if (null == mCityCid) {
        } else {
            for (int i = 0; i < mCities.size(); i++) {
                if (mCities.get(i).getCid().equals(mCityCid)) {
                    mWeatherPager.setCurrentItem(i);
                    return;
                }
            }
            mWeatherPager.setCurrentItem(0);
        }
    }

    @Override
    public void setPresenter(WeatherHomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showCitySelectUi() {
        Intent intent = new Intent(getContext(), CitySelectActivity.class);
        startActivityForResult(intent, CitySelectFragment.REQUEST_CODE);
    }

    @Override
    public void showSettingUi() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void showShareUi() {
        WeatherNow weatherNow = null;
        WeatherForecast weatherForecast = null;
        if (mPageAdapter.getCurrentView() != null) {
            weatherNow = ((WeatherFragment) mPageAdapter.getCurrentView()).mWeatherNowData;
            weatherForecast = ((WeatherFragment) mPageAdapter.getCurrentView()).mWeatherForecastData;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_share, null, false);
        view.layout(0, 0, width, height);
        if (weatherNow != null && weatherForecast != null) {
            TextView location = view.findViewById(R.id.location);
            location.setText(weatherNow.getLocation());
            TextView date = view.findViewById(R.id.date);
            date.setText(DateUtil.formatStringDate(weatherNow.getLoc(), DateUtil.FORMAT_DEFAULT, DateUtil.FORMAT_MMDD_CN));
            TextView temperature = view.findViewById(R.id.temperature_now);
            temperature.setText(weatherNow.getTmp());
            TextView cond = view.findViewById(R.id.cond_txt);
            cond.setText(weatherNow.getCondTxt());
            TextView temperatureRange = view.findViewById(R.id.temperature_range);
            temperatureRange.setText(String.format("%s/%s°", weatherForecast.tmpMax, weatherForecast.tmpMin));
        }
        int measureWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measureHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        view.measure(measureWidth, measureHeight);

        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        view.layout(0, 0, view.getWidth(), view.getHeight());
        view.draw(canvas);


        File file = FileUtils.saveImage(bitmap, getActivity());
        if (file != null) {
            intent.putExtra(Intent.EXTRA_STREAM, FileUtils.getShareUri(file, getActivity()));
            startActivity(Intent.createChooser(intent, "分享到"));
        } else {
            Toast.makeText(getActivity(), getString(R.string.abort_share), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void allCitiesLoaded(List<QueryItem> cities) {
        mWeatherPager.setVisibility(View.VISIBLE);
        KLog.d(cities);
        mCities = cities;
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < cities.size(); i++) {
            QueryItem city = cities.get(i);
            WeatherFragment fragment = WeatherFragment.getInstance(city.getCid(), city.getLocation(), city.isHome());
            fragments.add(fragment);
        }
        mPageAdapter.setNewFragments(fragments);
        mPageAdapter.notifyDataSetChanged();
        reselectItem();
    }

    @Override
    public void showNoCityUi() {
        mWeatherPager.setVisibility(View.GONE);
        KLog.d();
        mNoCityView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        KLog.d();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        KLog.d();
        super.onDetach();
    }

    @Override
    public void showToolbarTitle(String title, String subTitle, Boolean isHome) {
        if (mToolbar != null) {
            mToolbar.setTitle(title);
            mToolbar.setSubtitle(subTitle);
            if (isHome) {
                mActionBar.setDisplayShowHomeEnabled(true);
                mActionBar.setDisplayHomeAsUpEnabled(true);
                mToolbar.setNavigationIcon(R.drawable.ic_home_white_24dp);
            } else {
                mActionBar.setDisplayShowHomeEnabled(false);
                mActionBar.setDisplayHomeAsUpEnabled(false);
            }
        }
    }
}

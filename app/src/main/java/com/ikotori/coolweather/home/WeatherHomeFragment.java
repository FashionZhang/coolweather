package com.ikotori.coolweather.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ikotori.coolweather.R;

import com.ikotori.coolweather.cityselect.CitySelectActivity;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.source.local.CitiesLocalDataSource;
import com.ikotori.coolweather.data.source.local.CoolWeatherDatabase;
import com.ikotori.coolweather.data.source.local.WeatherLocalDataSource;
import com.ikotori.coolweather.data.source.remote.WeatherRemoteDataSource;
import com.ikotori.coolweather.data.source.repository.WeatherHomeRepository;
import com.ikotori.coolweather.home.weather.WeatherPagerAdapterPresenter;
import com.ikotori.coolweather.home.weather.WeatherFragment;
import com.ikotori.coolweather.util.AppExecutors;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherHomeFragment extends Fragment implements WeatherHomeContract.View{

    private ViewPager mWeatherPager;

    private WeatherHomeContract.Presenter mPresenter;

    // adapter实现了presenter接口,也是一个presenter
    private WeatherPagerAdapterPresenter<WeatherFragment> mPageAdapter;

    private View mNoCityView;

    private Toolbar mToolbar;

    public WeatherHomeFragment() {
        // Required empty public constructor
    }

    public static WeatherHomeFragment newInstance() {
        return new WeatherHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter.start();
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
                        WeatherRemoteDataSource.getInstance(new AppExecutors())));
        mWeatherPager.setAdapter(mPageAdapter);
        mToolbar = (Toolbar) root.findViewById(R.id.toolbar);
        ((WeatherHomeActivity)getActivity()).setSupportActionBar(mToolbar);
        setHasOptionsMenu(true);
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
        }
        return true;
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
        startActivity(intent);
    }

    @Override
    public void showSettingUi() {

    }

    @Override
    public void showShareUi() {

    }

    @Override
    public void allCitiesLoaded(List<QueryItem> cities) {
        KLog.d(cities);
        List<Fragment> fragments = new ArrayList<>();
        for (QueryItem city : cities) {
            WeatherFragment fragment = WeatherFragment.getInstance(city.getCid());
            fragments.add(fragment);
        }
        mPageAdapter.setNewFragments(fragments);
        mPageAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoCityUi() {
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
}

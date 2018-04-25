package com.ikotori.coolweather.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.cityselect.CitySelectActivity;
import com.ikotori.coolweather.data.QueryItem;

import com.ikotori.coolweather.data.entity.WeatherNow;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherHomeFragment extends Fragment implements WeatherHomeContract.View {

    private WeatherHomeContract.Presenter mPresenter;

    private ViewPager mWeatherPager;

    private List<Fragment> fragments = new ArrayList<>();

    private HomeViewPagerAdapter mPageAdapter;

    private ViewPager mWeatherContainerView;

    private List<QueryItem> mCities;

    public WeatherHomeFragment() {
        // Required empty public constructor
    }

    public static WeatherHomeFragment newInstance() {
        return new WeatherHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_weather_home, container, false);
        // TODO 在这bind view
        mWeatherPager = root.findViewById(R.id.weather_viewpager);
        mPageAdapter = new HomeViewPagerAdapter(getActivity().getSupportFragmentManager());
        mWeatherPager.setAdapter(mPageAdapter);
        mWeatherPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                KLog.d(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    public void showWeatherNowUi(WeatherNow weatherNow) {
//        WeatherFragment fragment = WeatherFragment.getInstance(weatherNow.toString());
//        mPageAdapter.addFragment(fragment);
//        mPageAdapter.notifyDataSetChanged();
        ((WeatherFragment) mPageAdapter.getCurrentFragment()).showWeatherNowUi(weatherNow);
    }

    @Override
    public void showNoWeatherUi() {

    }

    @Override
    public void showChangeLocation(int position) {

    }

    @Override
    public void allCitiesLoaded(List<QueryItem> cities) {
        mCities = cities;
        List<Fragment> newFragments = new ArrayList<>();
        for (QueryItem city : cities) {
            WeatherFragment fragment = WeatherFragment.getInstance(weatherFragmentPresenter, city.getCid());
            newFragments.add(fragment);
        }
        mPageAdapter.setNewFragments(newFragments);
        mPageAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoCityUi() {

    }

    private WeatherFragmentCallBack weatherFragmentPresenter = new WeatherFragmentCallBack() {
        @Override
        public void openCitySelect() {

        }

        @Override
        public void openSetting() {

        }

        @Override
        public void openShare() {

        }

        @Override
        public void loadWeatherNow(String cid) {
            mPresenter.loadWeatherNow(cid);
        }

        @Override
        public void changeLocation(int position) {

        }

        @Override
        public void loadAllCities() {

        }

        @Override
        public void start() {

        }
    };
    public interface WeatherFragmentCallBack extends WeatherHomeContract.Presenter {
    }

}

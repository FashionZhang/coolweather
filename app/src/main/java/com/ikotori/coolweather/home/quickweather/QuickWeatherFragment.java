package com.ikotori.coolweather.home.quickweather;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.citysearch.CitiesAdapter;
import com.ikotori.coolweather.citysearch.CitySearchFragment;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.ikotori.coolweather.home.WeatherHomeActivity;
import com.ikotori.coolweather.home.quickweather.Views.QuickWeatherNowViews;
import com.ikotori.coolweather.home.weather.Views.WeatherNowViews;
import com.socks.library.KLog;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuickWeatherFragment extends Fragment implements QuickWeatherContract.View {

    private QuickWeatherContract.Presenter mPresenter;

    private CitiesAdapter mCitiesAdapter;

    private QuickWeatherNowViews mWeatherNowViews;

    private LinearLayout mWeatherView;

    private RecyclerView mCitiesView;

    private LinearLayout mTipsLayoutView;

    private ImageView mTipsImageView;

    private TextView mTipsTextView;

    private Toolbar mToolbar;

    public QuickWeatherFragment() {
        // Required empty public constructor
    }


    public static QuickWeatherFragment newInstance() {
        QuickWeatherFragment fragment = new QuickWeatherFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_quick_weather, container, false);

        mToolbar = root.findViewById(R.id.toolbar);
        mToolbar.getMenu().clear();
        ((WeatherHomeActivity) getActivity()).setSupportActionBar(mToolbar);

        mCitiesView = root.findViewById(R.id.city_list);
        mTipsLayoutView = root.findViewById(R.id.tips);
        mTipsImageView = root.findViewById(R.id.tips_img);
        mTipsTextView = root.findViewById(R.id.tips_text);
        mWeatherView = root.findViewById(R.id.weather_container);

        mCitiesAdapter = new CitiesAdapter(mCitySelectListener);
        mCitiesView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mCitiesView.setAdapter(mCitiesAdapter);

        mWeatherNowViews = new QuickWeatherNowViews(root);
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.navigation_viewpager);
        if (fragment instanceof QuickWeatherFragment) {
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.search_menu);
        }
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.onActionViewExpanded();
        searchView.setQueryHint(getString(R.string.search_city_name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryCity(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryCity(newText);
                return true;
            }
        });
    }

    private void queryCity(@NonNull String query) {
        if ("".equals(query)) {

        } else {
            if (mWeatherView.getVisibility() == View.VISIBLE) {
                mWeatherView.setVisibility(View.GONE);
            }
            mPresenter.queryCity(query);
        }
    }

    private CitySearchFragment.QueryItemListener mCitySelectListener = new CitySearchFragment.QueryItemListener() {
        @Override
        public void onCityClick(QueryItem city) {
            mPresenter.citySelect(city);
        }
    };

    @Override
    public void setPresenter(QuickWeatherContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMatchCities(List<QueryItem> resultList) {
        KLog.d(resultList);
        changeTipsVisibility(View.GONE);
        mCitiesView.setVisibility(View.VISIBLE);
        mCitiesAdapter.setData(resultList);
    }

    private void changeTipsVisibility(int visible) {
        mTipsLayoutView.setVisibility(visible);
    }

    @Override
    public void showNoMatchCity() {
        changeTipsVisibility(View.VISIBLE);
        mTipsTextView.setText(R.string.no_match_result);
        mTipsImageView.setImageResource(R.drawable.ic_inbox_black_48dp);
    }

    @Override
    public void showCitySelect(QueryItem city) {
        changeTipsVisibility(View.GONE);
        mCitiesView.setVisibility(View.GONE);
        mPresenter.queryWeather(city.getCid());
    }

    @Override
    public void showWeather(WeatherNow now) {
        changeTipsVisibility(View.GONE);
        mWeatherView.setVisibility(View.VISIBLE);
        KLog.d(now);
//        mWeatherNowViews.setWeatherNow(now);
        mWeatherNowViews.setData(now);
    }

    @Override
    public void showWeatherNotAvailable() {
        changeTipsVisibility(View.VISIBLE);
        mTipsTextView.setText(R.string.weather_not_available);
        mTipsImageView.setImageResource(R.drawable.ic_not_interested_black_48dp);
    }

    @Override
    public void showGuideView() {
        changeTipsVisibility(View.VISIBLE);
        mTipsTextView.setText(R.string.quick_weather_guide);
        mTipsImageView.setImageResource(R.drawable.ic_search_black_48dp);
    }
}

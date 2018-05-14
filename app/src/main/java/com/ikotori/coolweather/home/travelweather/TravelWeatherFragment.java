package com.ikotori.coolweather.home.travelweather;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.citysearch.CitiesAdapter;
import com.ikotori.coolweather.citysearch.CitySearchFragment;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.CityWeather;
import com.socks.library.KLog;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TravelWeatherFragment extends Fragment implements TravelWeatherContract.View, View.OnClickListener {


    private TravelWeatherContract.Presenter mPresenter;

    private Button mConfirmView;

    private TextView mWeatherView;

    private Dialog mFullScreenDialog;

    private CitiesAdapter mCitiesAdapter;

    private RecyclerView mCityListView;

    private EditText mOriginRegionView;

    private EditText mDestinationView;

    private QueryItem mOriginCity;

    private QueryItem mDestinationCity;

    private SearchView searchView;

    private List<CityWeather> mCityWeathers;

    private TravelWeatherAdapter mWeatherAdapter;

    private RecyclerView mWeatherListView;

    private static final int ORIGIN = 0;

    private static final int DESTINATION = 1;

    public TravelWeatherFragment() {
        // Required empty public constructor
    }

    public static TravelWeatherFragment newInstance() {
        TravelWeatherFragment fragment = new TravelWeatherFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_travel_weather, container, false);

        mConfirmView = root.findViewById(R.id.confirm);
        mConfirmView.setOnClickListener(this);
        mWeatherView = root.findViewById(R.id.weather_result);
        mOriginRegionView = root.findViewById(R.id.origin_region);
        mDestinationView = root.findViewById(R.id.destination);
        mDestinationView.setFocusable(false);
        mDestinationView.setOnClickListener(this);
        mOriginRegionView.setFocusable(false);
        mOriginRegionView.setOnClickListener(this);

        mWeatherAdapter = new TravelWeatherAdapter();
        mWeatherListView = root.findViewById(R.id.weather_list);
        mWeatherListView.setVisibility(View.GONE);
        mWeatherListView.setLayoutManager(new LinearLayoutManager(getContext()));
        mWeatherListView.setAdapter(mWeatherAdapter);

        return root;
    }


    private void showFullScreenDialog(@Nullable String query, int type) {
       final int finalType = type;
        if (mFullScreenDialog == null) {
            mFullScreenDialog = new Dialog(getContext(), R.style.DialogFullscreen);
            mFullScreenDialog.setContentView(R.layout.dialog_search_fullscreen);

            ImageView closeView = mFullScreenDialog.findViewById(R.id.dialog_close);
            searchView = mFullScreenDialog.findViewById(R.id.dialog_searchview);
            mCityListView = mFullScreenDialog.findViewById(R.id.city_list);
            mCityListView.setLayoutManager(new LinearLayoutManager(getContext()));
            mCityListView.setAdapter(mCitiesAdapter);

            //搜索框默认展开
            searchView.setIconifiedByDefault(false);
            closeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFullScreenDialog.dismiss();
                }
            });
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (null != query && query.trim().length() > 0) {
                        mPresenter.queryCity(query.trim());
                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (null != newText && newText.trim().length() > 0) {
                        mPresenter.queryCity(newText);
                    }
                    return true;
                }
            });
        }

        searchView.setIconified(false);
        if (null != query) {
            searchView.setQuery(query, false);
            searchView.setQueryHint("");
        } else {
            //清除旧文本
            searchView.setIconified(true);
            searchView.setQueryHint(getString(R.string.destination));
        }

        mCitiesAdapter = new CitiesAdapter(new CitySearchFragment.QueryItemListener() {
            @Override
            public void onCityClick(QueryItem city) {
                if (finalType == ORIGIN) {
                    mOriginCity = city;
                    mOriginRegionView.setText(city.getLocation());
                } else {
                    mDestinationCity = city;
                    mDestinationView.setText(city.getLocation());
                }
                mFullScreenDialog.dismiss();
            }
        });
        mCityListView.setAdapter(mCitiesAdapter);
        mFullScreenDialog.show();
    }

    @Override
    public void setPresenter(TravelWeatherContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMatchCities(List<QueryItem> resultList) {
        mCitiesAdapter.setData(resultList);
        mCitiesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNoMatchCity() {

    }

    @Override
    public void showWeathers(List<CityWeather> weathers) {
        mCityWeathers = weathers;
        mWeatherAdapter.setData(weathers);
        mWeatherListView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoWeather() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
//                mPresenter.queryWeathers("117.227692", "31.822651", "115.858836", "28.690469");
                if (mOriginCity != null && mDestinationCity != null && mOriginCity.getCid() != null && mDestinationCity.getCid() != null) {
                    mPresenter.queryWeathers(mOriginCity.getLon(), mOriginCity.getLat(), mDestinationCity.getLon(), mDestinationCity.getLat());
                }
                break;
            case R.id.destination:
                showFullScreenDialog(null, DESTINATION);
                mWeatherListView.setVisibility(View.GONE);
                break;
            case R.id.origin_region:
                showFullScreenDialog(getString(R.string.my_location), ORIGIN);
                mWeatherListView.setVisibility(View.GONE);
        }
    }
}

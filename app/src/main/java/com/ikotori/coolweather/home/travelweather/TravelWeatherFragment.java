package com.ikotori.coolweather.home.travelweather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.CityWeather;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TravelWeatherFragment extends Fragment implements TravelWeatherContract.View, View.OnClickListener {


    private TravelWeatherContract.Presenter mPresenter;

    private Button mConfirmView;

    private TextView mWeatherView;

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
        return root;
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

    }

    @Override
    public void showNoMatchCity() {

    }

    @Override
    public void showWeathers(List<CityWeather> weathers) {
        mWeatherView.setText(weathers.toString());
    }

    @Override
    public void showNoWeather() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                mPresenter.queryWeathers("117.227692", "31.822651", "115.858836", "28.690469");
                break;
        }
    }
}

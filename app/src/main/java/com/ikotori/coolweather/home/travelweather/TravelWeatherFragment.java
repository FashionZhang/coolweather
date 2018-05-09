package com.ikotori.coolweather.home.travelweather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikotori.coolweather.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TravelWeatherFragment extends Fragment implements TravelWeatherContract.View {


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
        return inflater.inflate(R.layout.fragment_travel_weather, container, false);
    }

    @Override
    public void setPresenter(TravelWeatherContract.Presenter presenter) {


    }

    @Override
    public boolean isActive() {
        return false;
    }
}

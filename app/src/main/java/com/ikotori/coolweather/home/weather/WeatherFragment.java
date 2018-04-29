package com.ikotori.coolweather.home.weather;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.socks.library.KLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements WeatherContract.View {

    public final static String CID = "cid";
    public TextView mWeatherView;

    private String cid;

    private boolean isVisible;

    private WeatherContract.Presenter mPresenter;
    public WeatherFragment() {
        // Required empty public constructor
    }


    public static WeatherFragment getInstance(String cid) {
        Bundle arguments = new Bundle();
        arguments.putString(CID, cid);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_weather, container, false);
        mWeatherView = root.findViewById(R.id.weather_now);
        Bundle intent = getArguments();
        cid = intent.getString(CID);
        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        isVisible = isVisibleToUser;
        if (isVisibleToUser) {
            lazyLoad();
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyLoad() {
        if (isVisible && getView() != null) {
            mPresenter.start(cid, this);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        lazyLoad();
    }

    @Override
    public void setPresenter(WeatherContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isVisible && getView() != null;
    }

    @Override
    public void showDataNotAvailableUi() {
        mWeatherView.setText(cid);
    }

    @Override
    public void weatherNowLoaded(WeatherNow weatherNow) {
        KLog.d(this);
        mWeatherView.setText(weatherNow.toString());
    }

    @Override
    public void weatherNowNotAvailable() {
        mWeatherView.setText(getString(R.string.weather_now_not_available));
    }
}

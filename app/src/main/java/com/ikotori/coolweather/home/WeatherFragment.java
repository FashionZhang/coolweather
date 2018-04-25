package com.ikotori.coolweather.home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.WeatherNow;
import com.socks.library.KLog;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements WeatherHomeContract.View {

    public final static String CID = "cid";
    public TextView mWeatherView;

    private WeatherHomeFragment.WeatherFragmentCallBack mPresenter;
    private String cid;

    private boolean isVisible;
    public WeatherFragment() {
        // Required empty public constructor
    }


    public static WeatherFragment getInstance(@NonNull WeatherHomeFragment.WeatherFragmentCallBack callBack,String cid) {
        Bundle arguments = new Bundle();
        arguments.putString(CID, cid);
        WeatherFragment fragment = new WeatherFragment();
        fragment.setArguments(arguments);
        fragment.mPresenter = callBack;
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
        lazyLoad();
//        mWeatherView.setText(intent.getString("weather"));
        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = isVisibleToUser;
        if (isVisible) {
            lazyLoad();
        }

    }

    private void lazyLoad() {
        if (isVisible && getView() != null) {
            mPresenter.loadWeatherNow(cid);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setPresenter(WeatherHomeContract.Presenter presenter) {

    }

    @Override
    public boolean isActive() {
        return isResumed();
    }

    @Override
    public void showCitySelectUi() {

    }

    @Override
    public void showSettingUi() {

    }

    @Override
    public void showShareUi() {

    }

    @Override
    public void showWeatherNowUi(WeatherNow weatherNow) {
        mWeatherView.setText(weatherNow.toString());
        KLog.e("又重新请求了一次");
    }

    @Override
    public void showNoWeatherUi() {

    }

    @Override
    public void showChangeLocation(int position) {

    }

    @Override
    public void allCitiesLoaded(List<QueryItem> cities) {

    }

    @Override
    public void showNoCityUi() {

    }
}

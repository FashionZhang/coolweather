package com.ikotori.coolweather.home.weather.Views;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.WeatherHourly;

import java.util.List;

/**
 * Created by Fashion at 2018/05/02 23:33.
 * Describe:
 */

public class WeatherHourliesViews {
    public RecyclerView mRecyclerView;
    HourliesAdapter mAdapter;

    public WeatherHourliesViews(View view) {
        mRecyclerView = view.findViewById(R.id.container_hourlies);
        mAdapter = new HourliesAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setHourlies(List<WeatherHourly> hourlies) {
        mAdapter.setData(hourlies);
    }
}

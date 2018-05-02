package com.ikotori.coolweather.home.weather.Views;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.WeatherHourly;

import java.util.List;

/**
 * Created by Fashion at 2018/05/02 23:39.
 * Describe:
 */

public class HourliesAdapter extends RecyclerView.Adapter<HourliesAdapter.HourlyViewHolder> {

    private List<WeatherHourly> mHourlies;

    @Override
    public HourlyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_hourly, parent, false);
        return new HourlyViewHolder(view);
    }

    public void setData(List<WeatherHourly> hourlies) {
        mHourlies = hourlies;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(HourlyViewHolder holder, int position) {
        holder.setData(mHourlies.get(position));
    }

    @Override
    public int getItemCount() {
        return mHourlies == null ? 0 : mHourlies.size();
    }

    public static class HourlyViewHolder extends RecyclerView.ViewHolder {
        TextView mHourlyTimeView;
        TextView mHourlyCondView;
        TextView mHourlyTmpView;

        public HourlyViewHolder(View itemView) {
            super(itemView);
            mHourlyTimeView = itemView.findViewById(R.id.hourly_time);
            mHourlyCondView = itemView.findViewById(R.id.hourly_cond);
            mHourlyTmpView = itemView.findViewById(R.id.hourly_tmp);
        }

        public void setData(WeatherHourly hourly) {
            mHourlyTimeView.setText(hourly.getTime());
            mHourlyCondView.setText(hourly.getCondTxt());
            mHourlyTmpView.setText(hourly.getTmp());
        }
    }
}

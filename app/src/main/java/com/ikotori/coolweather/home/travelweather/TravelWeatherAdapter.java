package com.ikotori.coolweather.home.travelweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.CityWeather;
import com.ikotori.coolweather.data.entity.WeatherForecast;
import com.ikotori.coolweather.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fashion at 2018/05/14 15:11.
 * Describe:
 */

public class TravelWeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CityWeather> cityWeathers;

    public TravelWeatherAdapter() {
        cityWeathers = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_travel_item, parent, false);
        return new CityWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((CityWeatherViewHolder) holder).setData(cityWeathers.get(position));
    }

    @Override
    public int getItemCount() {
        return cityWeathers.size();
    }

    public void setData(List<CityWeather> cityWeathers) {
        this.cityWeathers = cityWeathers;
        notifyDataSetChanged();
    }

    public class CityWeatherViewHolder extends RecyclerView.ViewHolder {

        private final TextView mCondTxtView;
        private final TextView mLocationView;
        private final TextView mTmpView;
        private final TextView mTomorrowDateView;
        private final TextView mTomorrowCondTxtView;
        private final TextView mTomorrowTmpView;
        private final TextView mAfterTomorrowDateView;
        private final TextView mAfterTomorrowCondTxtView;
        private final TextView mAfterTomorrowTmpView;

        public CityWeatherViewHolder(View itemView) {
            super(itemView);
            mCondTxtView = itemView.findViewById(R.id.cond_txt);
            mLocationView = itemView.findViewById(R.id.location);
            mTmpView = itemView.findViewById(R.id.tmp);

            mTomorrowDateView = itemView.findViewById(R.id.day_1_date);
            mTomorrowCondTxtView = itemView.findViewById(R.id.day_1_condTxt);
            mTomorrowTmpView = itemView.findViewById(R.id.day_1_tmp);

            mAfterTomorrowDateView = itemView.findViewById(R.id.day_2_date);
            mAfterTomorrowCondTxtView = itemView.findViewById(R.id.day_2_condTxt);
            mAfterTomorrowTmpView = itemView.findViewById(R.id.day_2_tmp);
        }

        public void setData(CityWeather weather) {
            WeatherForecast today = weather.forecasts.get(0);
            WeatherForecast tomorrow = weather.forecasts.get(1);
            WeatherForecast afterTomorrow = weather.forecasts.get(2);
            mCondTxtView.setText(today.condTxtD);
            mLocationView.setText(weather.city.getLocation());
            mTmpView.setText(tmpFormat(today.tmpMax + "", today.tmpMin.toString()));

            mTomorrowDateView.setText(DateUtil.formatStringDate(tomorrow.date, DateUtil.FORMAT_YYYYMMDD, DateUtil.FORMAT_MMDD_CN));
            mTomorrowTmpView.setText(tmpFormat(tomorrow.tmpMax.toString(), tomorrow.tmpMin.toString()));
            mTomorrowCondTxtView.setText(tomorrow.condTxtD);

            mAfterTomorrowDateView.setText(DateUtil.formatStringDate(afterTomorrow.date, DateUtil.FORMAT_YYYYMMDD, DateUtil.FORMAT_MMDD_CN));
            mAfterTomorrowTmpView.setText(tmpFormat(afterTomorrow.tmpMax.toString(), afterTomorrow.tmpMin.toString()));
            mAfterTomorrowCondTxtView.setText(afterTomorrow.condTxtD);

        }

        private String tmpFormat(String max, String min) {
            return String.format("%s/%s°", max, min);
        }
    }
}

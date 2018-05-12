package com.ikotori.coolweather.cityselect;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.QueryItem;

import java.util.List;

/**
 * Created by Fashion at 2018/04/21 17:34.
 * Describe:
 */

public class CitySelectAdapter extends RecyclerView.Adapter<CitySelectAdapter.CitySelectViewHolder> implements OnMoveAndSwipedListener {

    private List<QueryItem> mCities;

    private CitySelectFragment.CitySelectListener mListener;

    private final int TYPE_HOME = 0;

    private final int TYPE_NORMAL = 1;

    public CitySelectAdapter(@NonNull CitySelectFragment.CitySelectListener listener) {
        mListener = listener;
    }

    public void setData(List<QueryItem> cities) {
        mCities = cities;
        notifyDataSetChanged();
    }
    @Override
    public CitySelectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_card_item, parent, false);
        return new CitySelectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CitySelectViewHolder holder, final int position) {
        final QueryItem city = mCities.get(position);
        holder.setData(city);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.citySelect(city);
            }
        });
        holder.mIsHomeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.setHome(position);
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HOME : TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return null == mCities ? 0 : mCities.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        mListener.onItemDismiss(position);
    }

    public static class CitySelectViewHolder extends RecyclerView.ViewHolder {

        private final TextView mLocationView;

        private final ImageView mIsHomeView;

        private final TextView mTemperatureWeaterView;

        public final View view;

        public CitySelectViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mLocationView = itemView.findViewById(R.id.location);
            mIsHomeView = itemView.findViewById(R.id.is_home);
            mTemperatureWeaterView = itemView.findViewById(R.id.temperature_weather);
        }


        public void setData(QueryItem city) {
            //TODO 天气和温度的处理
            mLocationView.setText(city.getLocation());

            if (city.isHome()) {
                mIsHomeView.setImageResource(R.drawable.ic_home_white_24dp);
            } else {
                mIsHomeView.setImageResource(R.drawable.ic_home_grey_500_24dp);
            }
        }
    }
}


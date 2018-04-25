package com.ikotori.coolweather.citysearch;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.QueryItem;

import java.util.List;

/**
 * Created by Fashion at 2018/04/20 23:18.
 * Describe:
 */

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

    public List<QueryItem> cities;
    private CitySearchFragment.QueryItemListener mQueryItemListener;


    public CitiesAdapter(@NonNull CitySearchFragment.QueryItemListener listener) {
        mQueryItemListener = listener;
    }

    public void setData(List<QueryItem> list) {
        cities = list;
        notifyDataSetChanged();
    }

    @Override
    public CitiesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.query_item, parent, false);
        return new ViewHolder(view, mQueryItemListener);
    }

    @Override
    public void onBindViewHolder(CitiesAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return cities == null ? 0 : cities.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mLocation;
        private final TextView mParentCity;
        private final TextView mAdminArea;
        private final TextView mCnty;
        private CitySearchFragment.QueryItemListener mListener;
        private int mPosition;

        public ViewHolder(View itemView, @NonNull CitySearchFragment.QueryItemListener listener) {
            super(itemView);
            mLocation = itemView.findViewById(R.id.location);
            mParentCity = itemView.findViewById(R.id.parentCity);
            mAdminArea = itemView.findViewById(R.id.adminArea);
            mCnty = itemView.findViewById(R.id.cnty);
            mListener = listener;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCityClick(cities.get(mPosition));
                }
            });
        }

        public void setData(int position) {
            mPosition = position;
            QueryItem item = cities.get(position);
            mLocation.setText(item.getLocation());
            if (null == item.getParentCity() || "".equals(item.getParentCity())) {
                mParentCity.setVisibility(View.GONE);
            } else {
                mParentCity.setText(item.getParentCity());
            }
            mAdminArea.setText(item.getAdminArea());
            mCnty.setText(item.getCnty());
        }
    }
}
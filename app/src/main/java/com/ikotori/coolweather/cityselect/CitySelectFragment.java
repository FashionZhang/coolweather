package com.ikotori.coolweather.cityselect;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.citysearch.CitySearchActivity;
import com.ikotori.coolweather.data.QueryItem;
import com.socks.library.KLog;

import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CitySelectFragment extends Fragment implements CitySelectContract.View {

    private CitySelectContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private View mNoCityView;

    private CitySelectAdapter mAdapter;

    private List<QueryItem> mCities;

    public static CitySelectFragment newInstance() {
        return new CitySelectFragment();
    }

    public CitySelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.openCitySearch();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_city_select, container, false);
        mRecyclerView = root.findViewById(R.id.recyclerview);
        mNoCityView = root.findViewById(R.id.no_city);

        mAdapter = new CitySelectAdapter(mListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        return root;
    }

    @Override
    public void onResume() {
        mPresenter.start();
        super.onResume();
    }

    @Override
    public void setPresenter(CitySelectContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showCitySearchUi() {
        Intent intent = new Intent(getContext(), CitySearchActivity.class);
        startActivity(intent);
    }

    @Override
    public void showCitySelectUi(QueryItem city) {
        Snackbar.make(mNoCityView, "你点击了" + city.getLocation(), Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void showSetHomeUi(QueryItem city) {
        //TODO to home
        setAdapterData();
    }

    @Override
    public void showDeleteCityUi(final QueryItem city, final int position) {
        mCities.remove(position);
        mAdapter.notifyItemRemoved(position);
        Snackbar.make(mNoCityView, getString(R.string.dismissed), Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.insertCity(city);
                        mCities.add(position, city);
                        mAdapter.notifyItemInserted(position);
                    }
                })
                .show();
    }

    @Override
    public void showFailUi() {
        Snackbar.make(mRecyclerView, "操作失败", Snackbar.LENGTH_SHORT).show();
    }

    private void hideNoCity() {
        mNoCityView.setVisibility(View.GONE);
    }

    @Override
    public void showNoCity() {
        mNoCityView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCities(List<QueryItem> cities) {
        mCities = cities;
        setAdapterData();
    }

    private void setAdapterData() {
        if (null == mCities || mCities.size() == 0) {
            showNoCity();
        } else {
            for (int i = 0; i < mCities.size(); i++) {
                QueryItem homeCity = mCities.get(i);
                if (homeCity.isHome()) {
//                    mCities.remove(i);
//                    mCities.add(0, homeCity);
                    Collections.swap(mCities, i, 0);
                    break;
                }
            }
            hideNoCity();
        }
        mAdapter.setData(mCities);
    }

    private CitySelectListener mListener = new CitySelectListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            return false;
        }

        @Override
        public void onItemDismiss(int position) {
            mPresenter.deleteCity(mCities.get(position), position);
        }

        @Override
        public void citySelect(QueryItem item) {
            mPresenter.citySelect(item);
        }

        @Override
        public void setHome(int position) {
            if (position == 0) {

            } else {
                mCities.get(0).setHome(false);
                mCities.get(position).setHome(true);
                mPresenter.setHome(mCities.get(position), mCities.get(0));
            }
        }
    };

    public interface CitySelectListener extends OnMoveAndSwipedListener{
        void citySelect(QueryItem item);

        void setHome(int position);
    }
}

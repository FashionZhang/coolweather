package com.ikotori.coolweather.cityselect;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.citysearch.CitySearchActivity;
import com.ikotori.coolweather.data.QueryItem;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CitySelectFragment extends Fragment implements CitySelectContract.View {
    public static final int REQUEST_CODE = 1;

    public static final String PAGE_INDEX = "page_index";

    public static final String ADD_DELETE = "add_delete";

    private CitySelectContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private View mNoCityView;

    private CitySelectAdapter mAdapter;

    private List<QueryItem> mCities;

    //用来记录是否增加或删除城市,该结果会影响到主页的城市的重新加载
    private boolean mAdd = false;
    private int mDelete = 0;

    private Intent returnData = new Intent();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mAdd = true;
            updateAddDelete();
        }
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
        startActivityForResult(intent,CitySelectFragment.REQUEST_CODE);
    }

    @Override
    public void showCitySelectUi(QueryItem city) {
//        Snackbar.make(mNoCityView, "你点击了" + city.getLocation(), Snackbar.LENGTH_SHORT).show();
        //将当前选择的城市cid回传给主页.
        returnData.putExtra(PAGE_INDEX, city.getCid());
        returnData.putExtra(ADD_DELETE, mAdd | (mDelete > 0));
        getActivity().setResult(Activity.RESULT_OK, returnData);
        getActivity().finish();
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
                        mDelete -= 1;
                        updateAddDelete();
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
                if (homeCity.isHome() && i > 0) {
                    mCities.remove(i);
                    mCities.add(0, homeCity);
//                    Collections.swap(mCities, i, 0);
                    break;
                }
            }
            hideNoCity();
        }
        mAdapter.setData(mCities);
    }

    private void updateAddDelete() {
        ((CitySelectActivity) getActivity()).mAddDelete = mAdd | (mDelete>0);
    }

    private CitySelectListener mListener = new CitySelectListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            return false;
        }

        @Override
        public void onItemDismiss(int position) {
            mPresenter.deleteCity(mCities.get(position), position);
            mDelete += 1;
            updateAddDelete();
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

package com.ikotori.coolweather.citysearch;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.QueryItem;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CitySearchFragment extends Fragment implements CitySearchContract.View{


    private CitySearchContract.Presenter mPresenter;

    private CitiesAdapter mAdapter;

    private View mNoResultView;

    public static CitySearchFragment newInstance() {
        return new CitySearchFragment();
    }

    public CitySearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_city_search, container, false);
        setHasOptionsMenu(true);

        mAdapter = new CitiesAdapter(mQueryItemListener);
        RecyclerView recyclerView = root.findViewById(R.id.query_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter);

        mNoResultView = root.findViewById(R.id.noResult);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.onActionViewExpanded();
        searchView.setQueryHint(getString(R.string.search_city_name));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryCity(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryCity(newText);
                return true;
            }
        });
    }

    private void queryCity(@NonNull String query) {
        if ("".equals(query)) {

        } else {
            mPresenter.queryCity(query);
        }
    }

    @Override
    public void setPresenter(CitySearchContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showMatchResult(List<QueryItem> resultList) {
        mNoResultView.setVisibility(View.GONE);
        mAdapter.setData(resultList);
    }

    @Override
    public void showNoMatchResult() {
        mNoResultView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showResultItemSelect(QueryItem item) {
//        Snackbar.make(mNoResultView, "你点击了" + item.getLocation(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showInsertCitySuccessUi() {
//        Snackbar.make(mNoResultView, "选择成功", Snackbar.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void showInsertCityFailUi() {
        Snackbar.make(mNoResultView, "城市已存在", Snackbar.LENGTH_SHORT).show();
    }

    private QueryItemListener mQueryItemListener = new QueryItemListener() {
        @Override
        public void onCityClick(QueryItem city) {
            mPresenter.resultItemSelect(city);
        }
    };

    public interface QueryItemListener {
        void onCityClick(QueryItem city);
    }
}

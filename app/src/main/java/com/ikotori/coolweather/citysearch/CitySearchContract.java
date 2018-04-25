package com.ikotori.coolweather.citysearch;

import android.support.annotation.NonNull;

import com.ikotori.coolweather.BasePresenter;
import com.ikotori.coolweather.BaseView;
import com.ikotori.coolweather.data.QueryItem;

import java.util.List;

/**
 * Created by Fashion at 2018/04/20 08:52.
 * Describe:
 */

public interface CitySearchContract {

    interface View extends BaseView<Presenter> {

        void showMatchResult(List<QueryItem> resultList);

        void showNoMatchResult();

        void showResultItemSelect(QueryItem item);

        //用户点击了城市搜索结果列表中的一项时,需要将这个城市加入到数据库中,这时存在加入失败(城市已存在)或者加入成功两种反馈

        void showInsertCitySuccessUi();

        void showInsertCityFailUi();

    }


    interface Presenter extends BasePresenter {

        void queryCity(@NonNull String query);

        void resultItemSelect(QueryItem item);
    }
}

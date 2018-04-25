package com.ikotori.coolweather;

/**
 * Created by Fashion at 2018/04/17 21:53.
 * Describe:
 */

public interface BaseView<T> {
    void setPresenter(T presenter);

    boolean isActive();

}

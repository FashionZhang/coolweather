package com.ikotori.coolweather.cityselect;

/**
 * Created by Fashion at 2018/05/12 14:52.
 * Describe:
 */

public interface OnMoveAndSwipedListener {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);

}

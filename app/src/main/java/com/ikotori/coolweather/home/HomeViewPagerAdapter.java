package com.ikotori.coolweather.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fashion at 2018/04/22 10:37.
 * Describe:
 */

public class HomeViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments = new ArrayList<>();

    private Fragment currentFragment;

    public HomeViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment) {
        mFragments.add(fragment);
    }

    public void setFragments(List<Fragment> fragments) {
        mFragments = fragments;
    }

    public void setNewFragments(List<Fragment> fragments) {
        mFragments.clear();
        mFragments = fragments;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (mFragments.size() > 0) {
            currentFragment = mFragments.get(position);
            KLog.e(mFragments.get(position)+","+position);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}

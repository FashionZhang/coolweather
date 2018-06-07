package com.ikotori.coolweather.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fashion at 2018/05/07 11:15.
 * Describe:
 */

public class NavigationAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    private Object mCurrentFragment;

    public NavigationAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragmnet(Fragment fragment) {
        if (mFragments == null) {
            mFragments = new ArrayList<>();
        }
        mFragments.add(fragment);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = object;
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}

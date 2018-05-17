package com.ikotori.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by Fashion at 2018/04/18 22:55.
 * Describe:
 */

public class ActivityUtils {
    public static void addFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static String getVersionName(@NonNull Context context) {
        String versionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext().getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取温度单位设置,默认为1,即摄氏度
     * @param context
     * @return
     */
    public static Integer getTemperatureUnit(Context context) {
        SharedPreferences defaultSharePreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String tmp =defaultSharePreferences.getString("temperature_unit", "1");
        return Integer.valueOf(tmp);
    }
}

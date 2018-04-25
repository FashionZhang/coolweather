package com.ikotori.coolweather;

import android.app.Application;

import com.socks.library.KLog;

/**
 * Created by Fashion at 2018/04/21 23:17.
 * Describe:
 */

public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(BuildConfig.DEBUG);
    }
}

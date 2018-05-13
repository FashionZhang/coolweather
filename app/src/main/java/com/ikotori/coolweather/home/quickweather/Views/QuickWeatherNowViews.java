package com.ikotori.coolweather.home.quickweather.Views;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ikotori.coolweather.R;
import com.ikotori.coolweather.data.entity.WeatherNow;

/**
 * Created by Fashion at 2018/05/13 11:39.
 * Describe:
 */

public class QuickWeatherNowViews {

    private LinearLayout mContainer;

    private TextView mLocationView;

    private TextView mCondTxtView;

    private TextView mTmpView;

    private TextView mFlView;

    private TextView mWindDirView;

    private TextView mWindSpdView;

    private TextView mWindScView;

    private TextView mHumView;

    private TextView mPcpnView;

    private TextView mPresView;

    private TextView mVisView;

    private TextView mCloudView;

    private TextView mUpdateTimeView;


    public QuickWeatherNowViews(View root) {
        mContainer = root.findViewById(R.id.quick_weather_container);
        mCondTxtView = root.findViewById(R.id.cond_txt);
        mLocationView = root.findViewById(R.id.location);
        mTmpView = root.findViewById(R.id.tmp);
        mFlView = root.findViewById(R.id.fl);
        mWindDirView = root.findViewById(R.id.wind_dir);
        mWindScView = root.findViewById(R.id.wind_sc);
        mWindSpdView = root.findViewById(R.id.wind_speed);
        mPcpnView = root.findViewById(R.id.pcpn);
        mHumView = root.findViewById(R.id.hum);
        mVisView = root.findViewById(R.id.vis);
        mCloudView = root.findViewById(R.id.cloud);
        mPresView = root.findViewById(R.id.pres);
        mUpdateTimeView = root.findViewById(R.id.update_time);
//        setVisibility(false);
    }

    public void setVisibility(boolean visibility) {
        int visible = visibility ? View.VISIBLE : View.GONE;
        mContainer.setVisibility(visible);
    }

    public void setData(WeatherNow now) {
        mCondTxtView.setText(now.getCondTxt());
        mLocationView.setText(now.getLocation());
        mTmpView.setText(now.getTmp());
        mFlView.setText(now.getFl());
        mWindDirView.setText(now.getWindDir());
        mWindScView.setText(now.getWindSc());
        mWindSpdView.setText(now.getWindSpd());
        mHumView.setText(now.getHum());
        mPcpnView.setText(now.getPcpn());
        mVisView.setText(now.getVis());
        mCloudView.setText(now.getCloud());
        mPresView.setText(now.getPres());
        mUpdateTimeView.setText(now.getLoc());
//        setVisibility(true);
    }
}

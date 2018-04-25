package com.ikotori.coolweather.api;

import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Fashion at 2018/04/21 11:38.
 * Describe:
 */

public class HeWeatherApi {
    public final static String BODY_TAG = "HeWeather6";
    public final static String KEY = "f44a28a9fc8942089faf4a3095f56088";
    public final static String GROUP_DEFAULT = "cn";
    public final static String DEFAULT_LANG = "zh_cn";
    public final static String CITY_SEARCH_BASE = "https://search.heweather.com/find";

    public final static String WEATHER_NOW = "https://free-api.heweather.com/s6/weather/now";

    public static HeWeatherApi INSTANCE;

    public static HeWeatherApi getInstance() {
        if (null == INSTANCE) {
            synchronized (HeWeatherApi.class) {
                if (null == INSTANCE) {
                    INSTANCE = new HeWeatherApi();
                }
            }
        }
        return INSTANCE;
    }

    private HeWeatherApi() {

    }

    /**
     * 构建查询城市接口的请求url
     * @param prefix  查询url
     * @param location  查询的地区或城市
     * @param number  查询返回多少条结果,默认10条
     * @return
     */
    public String buildCitySearchUrl(String prefix, String location, @Nullable String number) {
        if (number == null) {
            number = "10";
        }
        return String.format("%s?key=%s&location=%s&group=%s&number=%s&lang=%s",
                prefix, KEY, Uri.encode(location), GROUP_DEFAULT, number, DEFAULT_LANG);
    }


    /**
     * 构建查询实时天气接口的请求url
     * @param location 城市cid,拼音,城市名等
     * @return
     */
    public String buildWeatherNowUrl(String location) {
        return String.format("%s?key=%s&location=%s", WEATHER_NOW, KEY, Uri.encode(location));
    }
}

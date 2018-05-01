package com.ikotori.coolweather.api;

import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Fashion at 2018/04/21 11:38.
 * Describe:
 */

public class HeWeatherApi {
    public static final String BODY_TAG = "HeWeather6";
    public static final String KEY = "f44a28a9fc8942089faf4a3095f56088";
    public static final String GROUP_DEFAULT = "cn";
    public static final String DEFAULT_LANG = "zh_cn";
    public static final String CITY_SEARCH_BASE = "https://search.heweather.com/find";

    //实况天气API
    public static final String WEATHER_NOW = "https://free-api.heweather.com/s6/weather/now";

    //3-10天天气预报API
    public static final String WEATHER_FORECAST = "https://free-api.heweather.com/s6/weather/forecast";

    //逐小时天气预报API
    public static final String WEATHER_HOURLY = "https://free-api.heweather.com/s6/weather/hourly";

    //天气质量实况API
    public static final String AIR_NOW = "https://free-api.heweather.com/s6/air/now";

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
     *
     * @param prefix   查询url
     * @param location 查询的地区或城市
     * @param number   查询返回多少条结果,默认10条
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
     *
     * @param location 城市cid,拼音,城市名等
     * @return
     */
    public String buildWeatherNowUrl(String location) {
        return String.format("%s?key=%s&location=%s", WEATHER_NOW, KEY, Uri.encode(location));
    }

    /**
     * 构建3-10天天气预报接口的请求url
     *
     * @param location
     * @return
     */
    public String buildWeatherForecast(String location) {
        return String.format("%s?key=%s&location=%s", WEATHER_FORECAST, KEY, Uri.encode(location));
    }

    /**
     * 构建逐小时天气预报接口的请求url
     *
     * @param location
     * @return
     */
    public String buildWeatherHourly(String location) {
        return String.format("%s?key=%s&location=%s", WEATHER_HOURLY, KEY, Uri.encode(location));
    }

    /**
     * 构建空气质量实况接口的请求url
     *
     * @param location
     * @return
     */
    public String buildAirNow(String location) {
        return String.format("%s?key=%s&location=%s", WEATHER_HOURLY, KEY, Uri.encode(location));
    }
}

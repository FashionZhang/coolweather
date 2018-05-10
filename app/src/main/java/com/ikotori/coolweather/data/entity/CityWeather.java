package com.ikotori.coolweather.data.entity;

import com.ikotori.coolweather.data.QueryItem;

import java.util.List;

/**
 * Created by Fashion at 2018/05/10 14:15.
 * Describe:旅途天气页展示的天气信息及地区信息
 */

public class CityWeather {

    public QueryItem city;

    public List<WeatherForecast> forecasts;


    @Override
    public String toString() {
        return "CityWeather{" +
                "city=" + city +
                ", forecasts=" + forecasts +
                '}';
    }
}

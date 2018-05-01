package com.ikotori.coolweather.data.entity;

import java.util.List;

/**
 * Created by Fashion at 2018/04/29 16:09.
 * Describe:
 */

public class Weather {
    public static final String FORMAT = "yyyy-MM-dd HH:mm";

    private String cid;

    private WeatherNow now;

    private List<WeatherHourly> weatherHourlies;

    private List<WeatherForecast> weatherForecasts;

    private AirNow airNow;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public WeatherNow getNow() {
        return now;
    }

    public void setNow(WeatherNow now) {
        this.now = now;
    }

    public List<WeatherHourly> getWeatherHourlies() {
        return weatherHourlies;
    }

    public void setWeatherHourlies(List<WeatherHourly> weatherHourlies) {
        this.weatherHourlies = weatherHourlies;
    }

    public List<WeatherForecast> getWeatherForecasts() {
        return weatherForecasts;
    }

    public void setWeatherForecasts(List<WeatherForecast> weatherForecasts) {
        this.weatherForecasts = weatherForecasts;
    }

    public AirNow getAirNow() {
        return airNow;
    }

    public void setAirNow(AirNow airNow) {
        this.airNow = airNow;
    }
}

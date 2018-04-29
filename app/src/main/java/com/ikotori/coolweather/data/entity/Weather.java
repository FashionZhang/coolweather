package com.ikotori.coolweather.data.entity;

/**
 * Created by Fashion at 2018/04/29 16:09.
 * Describe:
 */

public class Weather {
    private String cid;

    private WeatherNow now;

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
}

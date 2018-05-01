package com.ikotori.coolweather.data.source.local;

import android.arch.persistence.room.Dao;

/**
 * Created by Fashion at 2018/04/29 17:16.
 * Describe:
 */
@Dao
public interface WeatherDao extends WeatherNowDao, WeatherForecastDao, WeatherHourlyDao, AirNowDao {

}

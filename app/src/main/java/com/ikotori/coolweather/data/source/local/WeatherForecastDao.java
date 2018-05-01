package com.ikotori.coolweather.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ikotori.coolweather.data.entity.WeatherForecast;

import java.util.List;

/**
 * Created by Fashion at 2018/04/29 22:23.
 * Describe:
 */

@Dao
public interface WeatherForecastDao {

    @Query("SELECT * FROM  weather_forecast WHERE cid = :cid")
    List<WeatherForecast> getWeatherForecastsByCid(String cid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeatherForecasts(List<WeatherForecast> weatherForecasts);

    @Delete
    void deleteWeatherForecasts(List<WeatherForecast> weatherForecasts);

}

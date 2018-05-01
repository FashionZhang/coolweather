package com.ikotori.coolweather.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ikotori.coolweather.data.entity.WeatherHourly;

import java.util.List;

/**
 * Created by Fashion at 2018/04/29 21:21.
 * Describe:
 */

@Dao
public interface WeatherHourlyDao {

    @Query("SELECT * FROM weather_hourly WHERE cid = :cid")
    List<WeatherHourly> getWeatherHourliesByCid(String cid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeatherHourlies(List<WeatherHourly> weatherHourlies);

    @Delete
    void deleteWeatherHourlies(List<WeatherHourly> weatherHourlies);
}

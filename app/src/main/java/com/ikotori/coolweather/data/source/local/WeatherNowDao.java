package com.ikotori.coolweather.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ikotori.coolweather.data.entity.WeatherNow;

import java.util.List;

/**
 * Created by Fashion at 2018/04/22 12:17.
 * Describe:
 */

@Dao
public interface WeatherNowDao {

    @Query("SELECT * FROM weather_now")
    List<WeatherNow> getAllCityWeather();

    @Query("SELECT * FROM weather_now WHERE cid = :cid")
    WeatherNow getCityWeather(String cid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCityWeather(WeatherNow now);

    @Update
    int updateCityWeather(WeatherNow now);

    @Query("DELETE FROM weather_now WHERE cid = :cid")
    int deleteByCid(String cid);

    @Query("SELECT COUNT(cid) FROM weather_now")
    int countCities();
}

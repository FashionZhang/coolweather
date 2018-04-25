package com.ikotori.coolweather.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ikotori.coolweather.data.QueryItem;

import java.util.List;

/**
 * Created by Fashion at 2018/04/21 14:26.
 * Describe:
 */
@Dao
public interface CitiesDao {

    @Query("SELECT * FROM cities")
    List<QueryItem> getCities();

    @Query("SELECT * FROM cities WHERE cid = :cid")
    QueryItem getCityById(String cid);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insertCity(QueryItem city);

    //主要用于更新城市是否为home
    @Update
    int updateCity(QueryItem city);

    @Query("DELETE FROM cities WHERE cid = :cid")
    int deleteTaskById(String cid);

    @Query("SELECT COUNT(cid) FROM cities")
    int countCities();
}

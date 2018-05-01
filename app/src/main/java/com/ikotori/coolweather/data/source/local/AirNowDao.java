package com.ikotori.coolweather.data.source.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ikotori.coolweather.data.entity.AirNow;

/**
 * Created by Fashion at 2018/04/29 22:57.
 * Describe:
 */

@Dao
public interface AirNowDao {

    @Query("SELECT * FROM air_now WHERE cid = :cid")
    AirNow getAirNowByCid(String cid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAirNow(AirNow airNow);

    @Delete
    void deleteAirNow(AirNow airNow);

}

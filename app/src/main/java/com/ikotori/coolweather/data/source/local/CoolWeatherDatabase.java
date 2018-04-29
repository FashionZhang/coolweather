package com.ikotori.coolweather.data.source.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.ikotori.coolweather.data.QueryItem;
import com.ikotori.coolweather.data.entity.WeatherNow;

/**
 * Created by Fashion at 2018/04/21 14:43.
 * Describe:
 */
@Database(entities = {QueryItem.class, WeatherNow.class}, version = 2, exportSchema = false)
public abstract class CoolWeatherDatabase extends RoomDatabase {

    private static CoolWeatherDatabase INSTANCE;

    public abstract CitiesDao citiesDao();

    public abstract WeatherDao weatherDao();

    private static final Object slock = new Object();

    public static CoolWeatherDatabase getInstance(Context context) {
        synchronized (slock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        CoolWeatherDatabase.class, "CoolWeather.db")
                        .fallbackToDestructiveMigration()
                        .build();
            }
            return INSTANCE;
        }
    }

}

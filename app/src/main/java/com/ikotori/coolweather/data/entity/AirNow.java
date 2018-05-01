package com.ikotori.coolweather.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fashion at 2018/04/29 22:43.
 * Describe:空气质量实况
 */

@Entity(tableName = "air_now")
public class AirNow {

    @PrimaryKey
    @NonNull
    public String cid;

    @ColumnInfo(name = "pub_time")
    @SerializedName("pub_time")
    //数据发布时间
    public String pubTime;

    //空气质量指数，AQI和PM25的关系
    public String aqi;

    //主要污染物
    public String main;

    //空气质量
    public String qlty;

    public String pm10;

    public String pm25;

    //二氧化氮
    public String no2;

    //二氧化硫
    public String so2;

    //一氧化碳
    public String co;

    //臭氧
    public String o3;

    public String loc;
}

package com.ikotori.coolweather.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Fashion at 2018/04/29 21:00.
 * Describe:逐小时天气
 */

@Entity(tableName = "weather_hourly")
public class WeatherHourly implements Serializable {

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "cid")
    @NonNull
    //城市id
    private String cid;

    @ColumnInfo(name = "time")
    //预报时间，格式yyyy-MM-dd HH:mm
    private String time;

    @ColumnInfo(name = "tmp")
    //温度
    private String tmp;

    @ColumnInfo(name = "cond_code")
    @SerializedName("cond_code")
    //天气状况代码
    private String condCode;

    @ColumnInfo(name = "cond_txt")
    @SerializedName("cond_txt")
    //实况天气状况描述,示例: 晴
    private String condTxt;

    @ColumnInfo(name = "wind_deg")
    @SerializedName("wind_deg")
    //风向360角度
    private String windDeg;

    @ColumnInfo(name = "wind_dir")
    @SerializedName("wind_dir")
    //风向
    private String windDir;

    @ColumnInfo(name = "wind_sc")
    @SerializedName("wind_sc")
    //风力
    private String windSc;

    @ColumnInfo(name = "wind_spd")
    @SerializedName("wind_spd")
    //风速,公里/小时
    private String windSpd;

    @ColumnInfo(name = "hum")
    //相对湿度
    private String hum;

    @ColumnInfo(name = "pres")
    //大气压强
    private String pres;

    @ColumnInfo(name = "pop")
    //降水概率,百分比
    private String pop;

    @ColumnInfo(name = "dew")
    //露点温度
    private String dew;

    @ColumnInfo(name = "cloud")
    //云量
    private String cloud;

    @ColumnInfo(name = "loc")
    //更新时间,当地时间
    private String loc;

    @Ignore
    public WeatherHourly(@NonNull String cid, String time, String tmp, String condCode, String condTxt, String windDeg, String windDir, String windSc, String windSpd, String hum, String pres, String pop, String dew, String cloud, String loc) {
        this.cid = cid;
        this.time = time;
        this.tmp = tmp;
        this.condCode = condCode;
        this.condTxt = condTxt;
        this.windDeg = windDeg;
        this.windDir = windDir;
        this.windSc = windSc;
        this.windSpd = windSpd;
        this.hum = hum;
        this.pres = pres;
        this.pop = pop;
        this.dew = dew;
        this.cloud = cloud;
        this.loc = loc;
    }

    public WeatherHourly(int uid, @NonNull String cid, String time, String tmp, String condCode, String condTxt, String windDeg, String windDir, String windSc, String windSpd, String hum, String pres, String pop, String dew, String cloud, String loc) {
        this.uid = uid;
        this.cid = cid;
        this.time = time;
        this.tmp = tmp;
        this.condCode = condCode;
        this.condTxt = condTxt;
        this.windDeg = windDeg;
        this.windDir = windDir;
        this.windSc = windSc;
        this.windSpd = windSpd;
        this.hum = hum;
        this.pres = pres;
        this.pop = pop;
        this.dew = dew;
        this.cloud = cloud;
        this.loc = loc;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @NonNull
    public String getCid() {
        return cid;
    }

    public void setCid(@NonNull String cid) {
        this.cid = cid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getCondCode() {
        return condCode;
    }

    public void setCondCode(String condCode) {
        this.condCode = condCode;
    }

    public String getCondTxt() {
        return condTxt;
    }

    public void setCondTxt(String condTxt) {
        this.condTxt = condTxt;
    }

    public String getWindDeg() {
        return windDeg;
    }

    public void setWindDeg(String windDeg) {
        this.windDeg = windDeg;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindSc() {
        return windSc;
    }

    public void setWindSc(String windSc) {
        this.windSc = windSc;
    }

    public String getWindSpd() {
        return windSpd;
    }

    public void setWindSpd(String windSpd) {
        this.windSpd = windSpd;
    }

    public String getHum() {
        return hum;
    }

    public void setHum(String hum) {
        this.hum = hum;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getDew() {
        return dew;
    }

    public void setDew(String dew) {
        this.dew = dew;
    }

    public String getCloud() {
        return cloud;
    }

    public void setCloud(String cloud) {
        this.cloud = cloud;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}

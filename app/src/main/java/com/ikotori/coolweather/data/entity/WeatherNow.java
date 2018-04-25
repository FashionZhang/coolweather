package com.ikotori.coolweather.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Fashion at 2018/04/22 11:23.
 * Describe: 实况天气
 */

@Entity(tableName = "weather_now")
public class WeatherNow implements Serializable {
    public static final String FORMAT = "yyyy-MM-dd HH:mm";

    @ColumnInfo(name = "cid")
    @PrimaryKey
    @NonNull
    //城市id
    private String cid;

    @ColumnInfo(name = "fl")
    // 体感温度,默认:摄氏度
    private String fl;

    @ColumnInfo(name = "tmp")
    // 温度,默认:摄氏度
    private String tmp;

    @ColumnInfo(name = "cond_code")
    @SerializedName("cond_code")
    //实况天气状况代码
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

    @ColumnInfo(name = "pcpn")
    //降水量
    private String pcpn;

    @ColumnInfo(name = "pres")
    //大气压强
    private String pres;

    @ColumnInfo(name = "vis")
    //能见度,默认单位:公里
    private String vis;

    @ColumnInfo(name = "cloud")
    //云量
    private String cloud;

    @ColumnInfo(name = "loc")
    //更新时间,当地时间
    private String loc;

    @ColumnInfo(name = "utc")
    //更新时间,UTC时间
    private String utc;

    private String location;

    public WeatherNow(@NonNull String cid, String fl, String tmp, String condCode, String condTxt, String windDeg, String windDir, String windSc, String windSpd, String hum, String pcpn, String pres, String vis, String cloud, String loc, String utc) {
        this.cid = cid;
        this.fl = fl;
        this.tmp = tmp;
        this.condCode = condCode;
        this.condTxt = condTxt;
        this.windDeg = windDeg;
        this.windDir = windDir;
        this.windSc = windSc;
        this.windSpd = windSpd;
        this.hum = hum;
        this.pcpn = pcpn;
        this.pres = pres;
        this.vis = vis;
        this.cloud = cloud;
        this.loc = loc;
        this.utc = utc;
    }

    @NonNull
    public String getCid() {
        return cid;
    }

    public void setCid(@NonNull String cid) {
        this.cid = cid;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
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

    public String getPcpn() {
        return pcpn;
    }

    public void setPcpn(String pcpn) {
        this.pcpn = pcpn;
    }

    public String getPres() {
        return pres;
    }

    public void setPres(String pres) {
        this.pres = pres;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
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

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "WeatherNow{" +
                "cid='" + cid + '\'' +
                ", fl='" + fl + '\'' +
                ", tmp='" + tmp + '\'' +
                ", condCode='" + condCode + '\'' +
                ", condTxt='" + condTxt + '\'' +
                ", windDeg='" + windDeg + '\'' +
                ", windDir='" + windDir + '\'' +
                ", windSc='" + windSc + '\'' +
                ", windSpd='" + windSpd + '\'' +
                ", hum='" + hum + '\'' +
                ", pcpn='" + pcpn + '\'' +
                ", pres='" + pres + '\'' +
                ", vis='" + vis + '\'' +
                ", cloud='" + cloud + '\'' +
                ", loc='" + loc + '\'' +
                ", utc='" + utc + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}

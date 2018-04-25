package com.ikotori.coolweather.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Fashion at 2018/04/20 15:28.
 * Describe:城市/地区信息
 */

@Entity(tableName = "cities")
public class QueryItem implements Serializable {

    @ColumnInfo(name = "location")
    @Nullable
    //城市/地区名称
    private String location;

    @ColumnInfo(name = "cid")
    @NonNull
    @PrimaryKey
    //城市id
    private String cid;

    @ColumnInfo(name = "lon")
    @Nullable
    //城市纬度
    private String lon;

    @ColumnInfo(name = "lat")
    @Nullable
    //城市经度
    private String lat;

    @ColumnInfo(name = "parent_city")
    //城市的上级城市
    @SerializedName("parent_city")
    private String parentCity;

    @ColumnInfo(name = "admin_area")
    //城市所属行政区域
    @SerializedName("admin_area")
    private String adminArea;

    @ColumnInfo(name = "cnty")
    //城市所属国家名称
    private String cnty;

    @ColumnInfo(name = "tz")
    //城市所在时区
    private String tz;

    @ColumnInfo(name = "type")
    //城市的属性,可选值为city城市和scenic中国景区
    private String type;

    @ColumnInfo(name = "is_home")
    private boolean isHome;

    @Ignore
    public QueryItem() {
    }

    @Ignore
    public QueryItem(String location, String parentCity, String adminArea, String cnty) {
        this.location = location;
        this.parentCity = parentCity;
        this.adminArea = adminArea;
        this.cnty = cnty;
    }

    public QueryItem(String location, @NonNull String cid, String lon, String lat, String parentCity, String adminArea, String cnty, String tz, String type, boolean isHome) {
        this.location = location;
        this.cid = cid;
        this.lon = lon;
        this.lat = lat;
        this.parentCity = parentCity;
        this.adminArea = adminArea;
        this.cnty = cnty;
        this.tz = tz;
        this.type = type;
        this.isHome = isHome;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getParentCity() {
        return parentCity;
    }

    public void setParentCity(String parentCity) {
        this.parentCity = parentCity;
    }

    public String getAdminArea() {
        return adminArea;
    }

    public void setAdminArea(String adminArea) {
        this.adminArea = adminArea;
    }

    public String getCnty() {
        return cnty;
    }

    public void setCnty(String cnty) {
        this.cnty = cnty;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isHome() {
        return isHome;
    }

    public void setHome(boolean home) {
        isHome = home;
    }


    @Override
    public String toString() {
        return "QueryItem{" +
                "location='" + location + '\'' +
                ", cid='" + cid + '\'' +
                ", lon='" + lon + '\'' +
                ", lat='" + lat + '\'' +
                ", parentCity='" + parentCity + '\'' +
                ", adminArea='" + adminArea + '\'' +
                ", cnty='" + cnty + '\'' +
                ", tz='" + tz + '\'' +
                ", type='" + type + '\'' +
                ", isHome=" + isHome +
                '}';
    }
}

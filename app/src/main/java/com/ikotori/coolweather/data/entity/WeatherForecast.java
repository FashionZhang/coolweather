package com.ikotori.coolweather.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fashion at 2018/04/29 21:57.
 * Describe:3-10天天气预报
 */

@Entity(tableName = "weather_forecast")
public class WeatherForecast {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    public String cid;

    //预报日期
    public String date;

    //日出时间
    public String sr;

    //日落时间
    public String ss;

    //月升时间
    public String mr;

    //月落时间
    public String ms;

    @ColumnInfo(name = "tmp_max")
    @SerializedName("tmp_max")
    //最高温度
    public Integer tmpMax;

    @ColumnInfo(name = "tmp_min")
    @SerializedName("tmp_min")
    //最低温度
    public Integer tmpMin;

    @ColumnInfo(name = "cond_code_d")
    @SerializedName("cond_code_d")
    //白天天气状况代码
    public Integer condCodeD;

    @ColumnInfo(name = "cond_code_n")
    @SerializedName("cond_code_n")
    //晚间天气状况代码
    public Integer condCodeN;

    @ColumnInfo(name = "cond_txt_d")
    @SerializedName("cond_txt_d")
    //白天天气状况描述
    public String condTxtD;

    @ColumnInfo(name = "cond_txt_n")
    @SerializedName("cond_txt_n")
    //晚间天气状况描述
    public String condTxtN;

    @ColumnInfo(name = "wind_deg")
    @SerializedName("wind_deg")
    //风向360角度
    public Integer windDeg;

    @ColumnInfo(name = "wind_dir")
    @SerializedName("wind_dir")
    //风向
    public String windDir;

    @ColumnInfo(name = "wind_sc")
    @SerializedName("wind_sc")
    //风力
    public String windSc;

    @ColumnInfo(name = "wind_spd")
    @SerializedName("wind_spd")
    //风速,公里/小时
    public Integer windSpd;

    @ColumnInfo(name = "hum")
    @SerializedName("hum")
    //相对湿度
    public Integer hum;

    //降水量
    public String pcpn;

    //降水概率
    public String pop;

    //大气压强
    public String pres;

    @ColumnInfo(name = "uv_index")
    @SerializedName("uv_index")
    //能见度,单位:公里
    public String uvIndex;

    //当地时间，24小时制，格式yyyy-MM-dd HH:mm
    public String loc;


    @Override
    public String toString() {
        return "WeatherForecast{" +
                "uid=" + uid +
                ", cid='" + cid + '\'' +
                ", date='" + date + '\'' +
                ", sr='" + sr + '\'' +
                ", ss='" + ss + '\'' +
                ", mr='" + mr + '\'' +
                ", ms='" + ms + '\'' +
                ", tmpMax=" + tmpMax +
                ", tmpMin=" + tmpMin +
                ", condCodeD=" + condCodeD +
                ", condCodeN=" + condCodeN +
                ", condTxtD='" + condTxtD + '\'' +
                ", condTxtN='" + condTxtN + '\'' +
                ", windDeg=" + windDeg +
                ", windDir='" + windDir + '\'' +
                ", windSc='" + windSc + '\'' +
                ", windSpd=" + windSpd +
                ", hum=" + hum +
                ", pcpn='" + pcpn + '\'' +
                ", pop='" + pop + '\'' +
                ", pres='" + pres + '\'' +
                ", uvIndex='" + uvIndex + '\'' +
                ", loc='" + loc + '\'' +
                '}';
    }
}

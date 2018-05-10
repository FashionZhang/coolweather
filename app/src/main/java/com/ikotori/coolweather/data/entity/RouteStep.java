package com.ikotori.coolweather.data.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Fashion at 2018/05/10 11:26.
 * Describe:百度地图驾车路线规划的路线步骤.
 * 详见  http://lbsyun.baidu.com/index.php?title=webapi/direction-api-v2#service-page-anchor-1-2
 */

public class RouteStep {

    @SerializedName("road_name")
    public String rodaName;

    /**
     * 枚举值：返回0-9之间的值
     * 0：高速路 1：城市高速路 2:国道 3：省道 4：县道 5：乡镇村道 6：其他道路 7：九级路 8：航线(轮渡) 9：行人道路
     */
    @SerializedName("road_type")
    public int roadType;

    @SerializedName("start_location")
    public Location startLocation;

    @SerializedName("end_location")
    public Location endLocation;

    public class Location {
        //经度
       public String lng;

        //纬度
       public String lat;
    }

    @Override
    public String toString() {
        return "RouteStep{" +
                "rodaName='" + rodaName + '\'' +
                ", roadType=" + roadType +
                ", startLocation=" + startLocation +
                ", endLocation=" + endLocation +
                '}';
    }
}

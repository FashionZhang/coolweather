package com.ikotori.coolweather.api;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

/**
 * Created by Fashion at 2018/05/09 22:37.
 * Describe:
 */

public class BaiduMapWebApi {
    private static final String AK = "cCx5CsvMHRdFsQm2om1Mg5OFYUL0ghsY";

    //驾车路线规划
    private static final String DRIVING_ROUTES = "http://api.map.baidu.com/direction/v2/driving";

    /**
     * 逆地理编码,即通过经纬度获得具体的行政区划数据
     * <p>
     * 详见  http://lbsyun.baidu.com/index.php?title=webapi/guide/webservice-geocoding-abroad
     */
    private static final String GEOCODER = "http://api.map.baidu.com/geocoder/v2/";

    public static BaiduMapWebApi INSTANCE;

    public static BaiduMapWebApi getInstance() {
        if (INSTANCE == null) {
            synchronized (BaiduMapWebApi.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BaiduMapWebApi();
                }
            }
        }
        return INSTANCE;
    }

    private BaiduMapWebApi() {

    }

    /**
     * 构建驾车路线规划url
     *
     * @param originLon      起始点经度
     * @param originLat      起始点纬度
     * @param destinationLon 目的地经度
     * @param destinationLat 目的地纬度
     * @return
     */
    public String buildDrivingRoutesUrl(@NonNull String originLon, @NonNull String originLat, @NonNull String destinationLon, @NonNull String destinationLat) {
        return String.format("%s?origin=%s,%s&destination=%s,%s&ak=%s",
                DRIVING_ROUTES, lonLatFormat(originLat), lonLatFormat(originLon),
                lonLatFormat(destinationLat), lonLatFormat(destinationLon), AK);
    }

    /**
     * 经纬度不能超过6位小数
     *
     * @param text
     * @return
     */
    @SuppressLint("DefaultLocale")
    private String lonLatFormat(String text) {
        int a = text.indexOf(".");
        int b = text.length() - 1;
        if ((b - a > 6)) {
            Double temp = Double.valueOf(text);
            text = String.format("%.6f", temp);
        }
        return text;
    }

    /**
     * 构建逆地理编码请求url,
     * <p>
     * 由于经纬度使用的是百度提供的经纬度
     * 所以不需要进行格式化,非百度回传经纬度请确保小数点后不超过6位
     * </p>
     *
     * @param lon 经度
     * @param lat 纬度
     * @return
     */
    public String buildGeoCoderUrl(@NonNull String lon, @NonNull String lat) {
        return String.format("%s?location=%s,%s&output=json&ak=%s",
                GEOCODER, lat, lon, AK);
    }
}

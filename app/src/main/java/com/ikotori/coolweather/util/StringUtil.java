package com.ikotori.coolweather.util;


/**
 * Created by Fashion at 2018/05/02 22:11.
 * Describe:
 */

public class StringUtil {

    /**
     * 摄氏度转华氏度
     * @param celsius
     * @return
     */
    public static String celsiusToFahrenheit(String celsius) {
        Double celsiusF = Double.valueOf(celsius);
        Double fahrenheitF = celsiusF * 1.8 + 32;
        return String.format("%.0f", fahrenheitF);
    }

    public static String celsiusToFahrenheit(Integer celsius) {
        return celsiusToFahrenheit(String.valueOf(celsius));
    }
}

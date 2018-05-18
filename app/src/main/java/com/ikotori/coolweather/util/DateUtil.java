package com.ikotori.coolweather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fashion at 2018/05/02 22:11.
 * Describe:
 */

public class DateUtil {
    public static String FORMAT_DEFAULT = "yyyy-MM-dd HH:mm";

    public static String FORMAT_YYYYMMDD = "yyyy-MM-dd";

    public static String FORMAT_MMDD_CN = "MM/dd";

    public static String FORMAT_HHMM = "HH:mm";

    public static String formatStringDate(String date, String originFormat, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(originFormat);
        try {
            Date d = dateFormat.parse(date);
            dateFormat.applyPattern(format);
            return dateFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatStringDate(String date) {
        return formatStringDate(date, FORMAT_DEFAULT, FORMAT_MMDD_CN);
    }

    public static String formatStringDate(String date, String originFormat) {
        return formatStringDate(date, originFormat, FORMAT_MMDD_CN);
    }
}

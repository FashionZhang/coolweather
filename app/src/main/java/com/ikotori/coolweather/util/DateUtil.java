package com.ikotori.coolweather.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    /**
     * 字符串类型日期转时间戳(秒)
     * 日期必须为"yyyy-MM-dd HH:mm"格式
     * @param dateStr
     * @return
     */
    public static long dateToTimestamp(String dateStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DEFAULT, Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(dateStr);
            return date.getTime()/1000L;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 计算两个日期相差的秒数
     * 日期必须为"yyyy-MM-dd HH:mm"格式
     * @param beginDate 起始日期
     * @param endDate  结束日期
     * @return 绝对值
     */
    public static long secondsBetween(String beginDate, String endDate) {
        return Math.abs(dateToTimestamp(endDate) - dateToTimestamp(beginDate));
    }

    /**
     * 计算给定日期与当前时间相差的秒数
     * @param beginDate
     * @return
     */
    public static long secondsFromNow(String beginDate) {
        return Math.abs(System.currentTimeMillis() / 1000L - dateToTimestamp(beginDate));
    }
}

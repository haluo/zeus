package com.soa.zeus.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: shizhizhong
 * Date: 13-11-7
 * Time: 上午10:00
 * To change this template use File | Settings | File Templates.
 */
public class DateTools extends org.apache.commons.lang.time.DateFormatUtils {
    public static String format(Date date) {
        if (date == null) {
            return null;
        }
        String s = org.apache.commons.lang.time.DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
        if (s == null) {
            return org.apache.commons.lang.time.DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return s;
    }


    public static final Date parseDate(String strDate, String format) {

        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(format);

        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
        }

        return (date);
    }

    /**
     * 按指定格式取得当前日期字符串
     *
     * @param pattern 日期格式
     * @return
     */
    public static final String getDateStringByPattern(String pattern) {
        Date date = new Date();
        return getDateStringByPattern(date, pattern);
    }
    /**
     * 按指定格式取得当前日期类型
     *
     * @param pattern 日期格式
     * @return
     */
    public static final Date getCurrentDate(String pattern) {
        Date date = new Date();
        String dateStr = getDateStringByPattern(date, pattern);
        return getDateByPattern(dateStr, pattern);
    }

    public static final Calendar getCurrentCalendar() {
        return Calendar.getInstance();
    }

    /**
     * 将日期字符串转化为Date类型
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static final Date getDateByPattern(String strDate, String pattern) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 将日期按照指定格式进行格式化
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    public static final String getDateStringByPattern(Date date, String pattern) {
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        String str = sf.format(date);
        return str;
    }

    public static Date formatDate(Date date, String format) {
        SimpleDateFormat inDf = new SimpleDateFormat(format);
        SimpleDateFormat outDf = new SimpleDateFormat(format);
        String reDate = "";
        try {
            reDate = inDf.format(date);
            return outDf.parse(reDate);
        } catch (Exception e) {

        }
        return date;
    }

    public static String get(){
        return "1212";
    }
}

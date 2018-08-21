package com.jit.appcloud.util;


import android.support.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by SilenceDut on 16/10/28.
 */

public class TimeUtil {
    private static SimpleDateFormat MONTH_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat HOUR_MINUTE = new SimpleDateFormat("HH:mm");
    private static SimpleDateFormat HOUR_MINUTE_SECOND = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat HOUR = new SimpleDateFormat("HH");
    private static SimpleDateFormat WEEK = new SimpleDateFormat("EEEE");
    private static SimpleDateFormat SIMPLE_TIME = new SimpleDateFormat("MM/dd");
    private static SimpleDateFormat SECOND_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat DAY_HOUR_MINUTE =new SimpleDateFormat("yy/MM/dd HH:mm");
    private static SimpleDateFormat DAY_HOUR = new SimpleDateFormat("MM/dd HH:mm");

    private static String AM_TIP = "";
    private static String PM_TIP = "";
    private static String YESTERDAY = "昨天";
    private static String BEFORE_YESTERDAY = "前天";
    public static final long DAY_OF_YEAR = 365;
    public static final long DAY_OF_MONTH = 30;
    public static final long HOUR_OF_DAY = 24;
    public static final long MIN_OF_HOUR = 60;
    public static final long SEC_OF_MIN = 60;
    public static final long MILLIS_OF_SEC = 1000;

    /**
     * 新时间戳显示
     * 0<X<1分钟     ：  刚刚
     * 1分钟<=X<60分钟    :    X分钟前         EX:5分钟前
     * 1小时<=X<24小时    ：  X小时前        EX：3小时前
     * 1天<=X<7天         ：   X天前           Ex： 4天前
     * 1周<=X<1个月     ：   X周前           EX：3周前
     * X>=1个月           :      YY/MM/DD   HH:MM       EX：15/05/15  15:34
     */


    public static long halfAnHourSeconds() {
        return MIN_OF_HOUR * SEC_OF_MIN / 2;
    }

    public static long anHourSeconds() {
        return MIN_OF_HOUR * SEC_OF_MIN;
    }

    public static long twoHourSeconds() {
        return MIN_OF_HOUR * SEC_OF_MIN * 2;
    }

    public static String getTimeTips(String formatTime) {
        String timeTips = formatTime;
        try {
            Date date = DATE_FORMAT.parse(formatTime);
            timeTips = getTimeTips(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeTips;
    }


    /* 或得当前时间*/
    public static String getCurrentTime() {
         return SECOND_FORMAT.format(new Date(System.currentTimeMillis()));
    }




    public static String getMopnthDay() {
        return MONTH_DAY_FORMAT.format(new Date());
    }

    public static String getHourMinute() {
        return HOUR_MINUTE.format(new Date());
    }

    public static String getWeek(String dataStr) {
        try {
            return WEEK.format(MONTH_DAY_FORMAT.parse(dataStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getSimpleTime(String dateStr){
        try {
            return SIMPLE_TIME.format(MONTH_DAY_FORMAT.parse(dateStr));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    private static String getTimeTips(long timeStamp) {
        long now = System.currentTimeMillis() / 1000;
        String tips;
        long diff = now - timeStamp / 1000;
        if (diff < 60) {
            tips = "刚刚";
        } else if ((diff /= 60) < 60) {
            tips = String.format("%d分钟前", diff);
        } else if ((diff /= 60) < 24) {
            tips = String.format("%d小时前", diff);
        } else if ((diff /= 24) < 7) {
            tips = String.format("%d天前", diff);
        } else if ((diff /= 7) < 4) {
            tips = String.format("%d周前", diff);
        } else {

            tips = DATE_FORMAT.format(new Date(timeStamp * 1000));

        }
        return tips;
    }

    public static String milliseconds2String(long milliSeconds) {
        return DATE_FORMAT.format(new Date(milliSeconds));
    }


    public static boolean isToday(long timestamp) {
        long millOfDay = MILLIS_OF_SEC * SEC_OF_MIN * MIN_OF_HOUR * HOUR_OF_DAY;
        return System.currentTimeMillis() / millOfDay == timestamp / millOfDay;
    }

    public static boolean isNight() {
        int currentHour = Integer.parseInt(HOUR.format(new Date()));
        return currentHour >= 19 || currentHour <= 6;
    }


    /**
     * 得到仿微信日期格式输出
     *
     * @param msgTimeMillis
     * @return
     */
    public static String getMsgFormatTime(long msgTimeMillis) {
        DateTime nowTime = new DateTime();
//        LogUtils.sf("nowTime = " + nowTime);
        DateTime msgTime = new DateTime(msgTimeMillis);
//        LogUtils.sf("msgTime = " + msgTime);
        int days = Math.abs(Days.daysBetween(msgTime, nowTime).getDays());
//        LogUtils.sf("days = " + days);
        if (days < 1) {
            //早上、下午、晚上 1:40
            return getTime(msgTime);
        } else if (days == 1) {
            //昨天
            return "昨天 " + getTime(msgTime);
        } else if (days <= 7) {
            //星期
            switch (msgTime.getDayOfWeek()) {
                case DateTimeConstants.SUNDAY:
                    return "周日 " + getTime(msgTime);
                case DateTimeConstants.MONDAY:
                    return "周一 " + getTime(msgTime);
                case DateTimeConstants.TUESDAY:
                    return "周二 " + getTime(msgTime);
                case DateTimeConstants.WEDNESDAY:
                    return "周三 " + getTime(msgTime);
                case DateTimeConstants.THURSDAY:
                    return "周四 " + getTime(msgTime);
                case DateTimeConstants.FRIDAY:
                    return "周五 " + getTime(msgTime);
                case DateTimeConstants.SATURDAY:
                    return "周六 " + getTime(msgTime);
            }
            return "";
        } else {
            //12月22日
            return msgTime.toString("MM月dd日 " + getTime(msgTime));
        }
    }

    @NonNull
    private static String getTime(DateTime msgTime) {
        int hourOfDay = msgTime.getHourOfDay();
        String when;
        if (hourOfDay >= 18) {//18-24
            when = "晚上";
        } else if (hourOfDay >= 13) {//13-18
            when = "下午";
        } else if (hourOfDay >= 11) {//11-13
            when = "中午";
        } else if (hourOfDay >= 5) {//5-11
            when = "早上";
        } else {//0-5
            when = "凌晨";
        }
        return when + " " + msgTime.toString("hh:mm");
    }

    /**
     * 把日期转换为字符串
     *
     * @param date
     * @return
     */
    public static String date2String(java.util.Date date, String format) {
        if(date == null){
            return "";
        }
        String result = "";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            result = formater.format(date);
        } catch (Exception e) {
            result = "";
        } finally {
            formater = null;
        }
        return result;
    }

    /**
     * 把符合日期格式的字符串转换为日期类型
     *
     * @param dateStr
     * @return
     */
    public static java.util.Date string2Date(String dateStr, String format) {
        Date d = null;
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            formater.setLenient(false);// 严格解析
            d = formater.parse(dateStr);
        } catch (Exception e) {
            d = null;
        } finally {
            formater = null;
        }
        return d;
    }

    public static String getMyTime(String time){
        Date date = string2Date(time, "yyyy-MM-dd HH:mm:ss");
        return date2String(date,"yy/MM/dd HH:mm");
    }

    public static String getMyTimeDay(String time){
        Date date = string2Date(time, "yyyy-MM-dd HH:mm:ss");
        return date2String(date,"yy/MM/dd");
    }

    public static synchronized String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        return MONTH_DAY_FORMAT.format(today);
    }

    public static String formatDateToMD(String str) {
        String formatStr = "";
        try {
            formatStr = DAY_HOUR.format(SECOND_FORMAT.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    public static String formatDateToMin(String str) {
        String formatStr = "";
        try {
            formatStr = HOUR_MINUTE_SECOND.format(SECOND_FORMAT.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

}

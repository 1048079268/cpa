package com.todaysoft.cpa.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @desc:
 * @author: 鱼唇的人类
 * @date: 2017/8/10 16:53
 */
public class DateUtil {
    public static String formatDate0(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateString=df.format(date);
        return dateString;
    }

    public static String formatDate1(Date date){
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String dateString=df.format(date);
        return dateString;
    }

    public static String formatDate2(Date date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString=df.format(date);
        return dateString;
    }

    /**
     * @desc: 得到昨天的日期字符串
     * @author: 鱼唇的人类
     * @return 格式：yyyyMMdd
     */
    public static String yesterday(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        Date date = calendar.getTime();
        return formatDate1(date);
    }

    /**
     * @desc: 时间字符串转换为Timestamp
     * @author: 鱼唇的人类
     * @param dateString 格式：yyyy-MM-dd
     * @return
     */
    public static Timestamp stringToTimestamp(String dateString){
        if (dateString!=null&&dateString.length()>0){
            SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd" );
            Date date= null;
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
            return new Timestamp(date.getTime());
        }
        return null;
    }

    public static String today(){
        return formatDate1(new Date());
    }
}

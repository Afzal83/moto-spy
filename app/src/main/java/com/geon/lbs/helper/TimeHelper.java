package com.geon.lbs.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Babu on 10/11/2016.
 */
public class TimeHelper {

    public static Date getStartTimeOfADay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    public static Date getEndTimOfADay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }
    public static float timeDifference(String startTime, String endtime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(startTime);
            d2 = format.parse(endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float diff = d2.getTime() - d1.getTime();
        return diff/(1000 * 60 * 60 * 24);
    }
    public static float timeDifferenceInHr(String startTime, String endtime){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(startTime);
            d2 = format.parse(endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        float diff = d2.getTime() - d1.getTime();
        return diff/(1000 * 60 * 60 );
    }
}

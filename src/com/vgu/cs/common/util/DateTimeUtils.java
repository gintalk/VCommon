package com.vgu.cs.common.util;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 15/03/2021
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateTimeUtils {
    
    private static final Map<String, SimpleDateFormat> FORMATTER_MAP;
    
    static {
        FORMATTER_MAP = new HashMap<>();
        _populateFormatterMap();
    }
    
    public static Date parseDate(String dateString) {
        return parse("yyyy/MM/dd", dateString);
    }
    
    public static Date parseDatetime(String datetimeString) {
        return parse("yyyy/MM/dd HH:mm:ss", datetimeString);
    }
    
    public static Date parse(String pattern, String source) {
        try {
            SimpleDateFormat formatter = _getDateFormatter(pattern);
            return formatter.parse(source);
        } catch (ParseException e) {
            return new Date();
        }
    }
    
    public static Date parseDateOrDateTime(String date, String dateTime) {
        if (StringUtils.isNullOrEmpty(dateTime)) {
            if (StringUtils.isNullOrEmpty(date)) {
                return null;
            }
            return parseDate(date);
        }
        return parseDatetime(dateTime);
    }
    
    public static String getDateString(Date date) {
        return getString("yyyy/MM/dd", date);
    }
    
    public static String getDateTimeString(Date date) {
        return getString("yyyy/MM/dd HH:mm:ss", date);
    }
    
    public static String getString(String pattern, Date date) {
        SimpleDateFormat formatter = _getDateFormatter(pattern);
        return formatter.format(date);
    }
    
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return calendar.get(Calendar.YEAR);
    }
    
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return calendar.get(Calendar.MONTH) + 1;
    }
    
    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        return calendar.get(Calendar.DAY_OF_MONTH);
    }
    
    public static void main(String[] args) {
        Date date = new Date(850461896000L);
        System.out.println(getDateTimeString(date));
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static void _populateFormatterMap() {
        FORMATTER_MAP.put("yyyy/MM/dd", new SimpleDateFormat("yyyy/MM/dd"));
        FORMATTER_MAP.put("yyyy/MM/dd HH:mm:ss", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
    }
    
    private static SimpleDateFormat _getDateFormatter(String pattern) {
        SimpleDateFormat formatter = FORMATTER_MAP.get(pattern);
        if (formatter == null) {
            formatter = new SimpleDateFormat(pattern);
            FORMATTER_MAP.put(pattern, formatter);
        }
        
        return formatter;
    }
}

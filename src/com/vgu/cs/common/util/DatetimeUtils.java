package com.vgu.cs.common.util;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 15/03/2021
 */

import com.vgu.cs.common.logger.VLogger;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeUtils {

    private static final Logger LOGGER = VLogger.getLogger(DatetimeUtils.class);

    public static Date parseDate(String dateString) {
        return parse("yyyy/MM/dd", dateString);
    }

    public static Date parseDatetime(String datetimeString) {
        return parse("yyyy/MM/dd HH:mm:ss", datetimeString);
    }

    public static Date parse(String pattern, String source) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(source);
        } catch (ParseException e) {
            LOGGER.error(e.getMessage(), e);
            return new Date();
        }
    }
}

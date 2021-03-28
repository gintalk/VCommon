package com.vgu.cs.common.util;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 28/03/2021
 */

import com.vgu.cs.common.common.StringBuilderPool;

public class LoggingUtils {

    public static String buildLog(String message, Object... objects) {
        StringBuilder logBuilder = StringBuilderPool.INSTANCE.borrowObject(100);

        try {
            logBuilder.append(Thread.currentThread().getStackTrace()[3].getMethodName());
            if (!StringUtils.isNullOrEmpty(message)) {
                logBuilder.append("\t\t");
                logBuilder.append(message);
            }
            for (Object object : objects) {
                logBuilder.append("\t\t");
                logBuilder.append(object);
            }

            return logBuilder.toString();
        } finally {
            StringBuilderPool.INSTANCE.returnObject(logBuilder);
        }
    }
}

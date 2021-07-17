package com.vgu.cs.common.util;

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

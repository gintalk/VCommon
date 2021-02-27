package com.vgu.cs.common.logger;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/02/2021
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VLogger {
    public static Logger getLogger(Class<?> clazz) {
        if (clazz == null) {
            return VEmptyLogger.INSTANCE;
        }

        Logger ret = LogManager.getLogger(clazz);
        return ret == null ? VEmptyLogger.INSTANCE : ret;
    }
}

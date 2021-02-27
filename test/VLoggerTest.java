/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/02/2021
 */

import com.vgu.cs.common.logger.VLogger;
import org.apache.logging.log4j.Logger;

public class VLoggerTest {

    private static final Logger LOGGER = VLogger.getLogger(VLoggerTest.class);

    public static void main(String[] args) {
        LOGGER.error("hehe");
    }
}

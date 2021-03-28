package com.vgu.cs.common.util;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/03/2021
 */

public class MathUtils {

    public static int binaryLogarithm(int n) {
        int log = 0;
        if ((n & -65536) != 0) {
            n >>>= 16;
            log = 16;
        }

        if (n >= 256) {
            n >>>= 8;
            log += 8;
        }

        if (n >= 16) {
            n >>>= 4;
            log += 4;
        }

        if (n >= 4) {
            n >>>= 2;
            log += 2;
        }

        return log + (n >>> 1);
    }
}

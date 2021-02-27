package com.vgu.cs.common.helper;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 27/02/2021
 */

import com.vgu.cs.common.common.thrift.TErrorCode;

public class ErrorHelper {

    public static boolean isSuccess(int errorCode){
        return TErrorCode.SUCCESS.getValue() == errorCode;
    }

    public static boolean isFail(int errorCode){
        return !isSuccess(errorCode);
    }
}

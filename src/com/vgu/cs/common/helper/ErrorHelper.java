package com.vgu.cs.common.helper;

import com.vgu.cs.common.common.thrift.TErrorCode;

public class ErrorHelper {

    public static boolean isSuccess(int errorCode){
        return TErrorCode.SUCCESS.getValue() == errorCode;
    }

    public static boolean isFail(int errorCode){
        return !isSuccess(errorCode);
    }
}

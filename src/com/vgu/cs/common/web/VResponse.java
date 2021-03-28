package com.vgu.cs.common.web;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/03/2021
 */

import com.vgu.cs.common.web.thrift.TStatusCode;

import java.util.HashMap;
import java.util.Map;

public class VResponse {

    private Object _data;
    private int _error;
    private String _message;
    private static final Map<Integer, String> MESSAGE_MAP;

    static {
        MESSAGE_MAP = new HashMap<>();
        MESSAGE_MAP.put(TStatusCode.FAIL.getValue(), "Lỗi hệ thống");
        MESSAGE_MAP.put(TStatusCode.SUCCESS.getValue(), "Thành công");
    }

    public VResponse(int error, String message){
        this(error, message, null);
    }

    public VResponse(int error, String message, Object data){
        _error = error;
        _message = message;
        _data = data;
    }

    public VResponse(TStatusCode statusCode){
        this(statusCode, null);
    }

    public VResponse(TStatusCode statusCode, Object data){
        _error = statusCode.getValue();
        _message = MESSAGE_MAP.get(_error);
        _data = data;
    }
}

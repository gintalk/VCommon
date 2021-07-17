package com.vgu.cs.common.web;

import com.vgu.cs.common.common.StringBuilderPool;
import com.vgu.cs.common.web.thrift.TStatusCode;

import java.util.HashMap;
import java.util.Map;

public class VResponse {

    private static final Map<Integer, String> MESSAGE_MAP;

    static {
        MESSAGE_MAP = new HashMap<>();
        MESSAGE_MAP.put(TStatusCode.FAIL.getValue(), "Lỗi hệ thống");
        MESSAGE_MAP.put(TStatusCode.SUCCESS.getValue(), "Thành công");
    }

    private final Object _data;
    private final int _error;
    private final String _message;

    public VResponse(int error, String message) {
        this(error, message, null);
    }

    public VResponse(int error, String message, Object data) {
        _error = error;
        _message = message;
        _data = data;
    }

    public VResponse(TStatusCode statusCode) {
        this(statusCode, null);
    }

    public VResponse(TStatusCode statusCode, Object data) {
        _error = statusCode.getValue();
        _message = MESSAGE_MAP.get(_error);
        _data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = StringBuilderPool.INSTANCE.borrowObject(1000);

        sb.append("{")
                .append("\"err\":").append(_error)
                .append(",\"msg\":\"").append(_message).append("\"")
                .append(",\"sTime\":").append(System.currentTimeMillis());
        if (_data != null) {
            sb.append(",\"data\":").append(_data);
        }
        sb.append("}");

        return sb.toString();
    }
}

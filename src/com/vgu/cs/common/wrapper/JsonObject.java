package com.vgu.cs.common.wrapper;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 29/03/2021
 */

import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.TypeLiteral;

import java.util.HashMap;
import java.util.Map;

public class JsonObject {

    private final Map<String, Any> _map;

    public JsonObject() {
        _map = new HashMap<>();
    }

    public JsonObject put(String key, Object value) {
        _map.put(key, Any.wrap(value));
        return this;
    }

    public int getInt(String key) {
        return _map.get(key).as(Integer.class);
    }

    public String getString(String key) {
        return _map.get(key).as(String.class);
    }

    public double getDouble(String key) {
        return _map.get(key).as(Double.class);
    }

    public long getLong(String key) {
        return _map.get(key).as(Long.class);
    }

    public float getFloat(String key) {
        return _map.get(key).as(Float.class);
    }

    public JsonObject getJsonObject(String key) {
        return _map.get(key).as(new TypeLiteral<JsonObject>() {
        });
    }

    @Override
    public String toString() {
        return JsonStream.serialize(_map);
    }
}

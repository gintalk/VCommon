package com.vgu.cs.common.common;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/03/2021
 */

import com.vgu.cs.common.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class StringBuilderPool {

    public static final StringBuilderPool INSTANCE;

    static {
        INSTANCE = new StringBuilderPool("main");
    }

    private final List<SimpleObjectPool<StringBuilder>> _pool;
    private final String _name;

    public StringBuilderPool(String name) {
        _pool = new ArrayList<>(32);
        _name = name;

        for (int i = 0; i < 31; i++) {
            int size = 1 << i;
            this._pool.add(new SimpleObjectPool<>(
                    _name + "-StringBuilderPool-" + size,
                    0,
                    () -> new StringBuilder(size)
            ));
        }
    }

    public StringBuilder borrowObject(int size) {
        if (size < 32) {
            size = 32;
        }

        int poolIndex = MathUtils.binaryLogarithm(size);
        if (size > 1 << poolIndex) {
            poolIndex++;
        }

        StringBuilder ret = null;
        for (int i = poolIndex; i < 31; ++i) {
            ret = this._pool.get(i).borrowObjectWoCtor();
        }

        if (ret == null) {
            if (poolIndex < 31) {
                ret = this._pool.get(poolIndex).borrowObject();
            } else {
                int finalSize = size;
                ret = this._pool.get(30).borrowObject(
                        (StringBuilder stringBuilder) -> stringBuilder.capacity() >= finalSize,
                        false
                );
                if (ret == null) {
                    ret = new StringBuilder(size);
                }
            }
        }

        return ret;
    }

    public void returnObject(StringBuilder object) {
        if (object == null) {
            return;
        }

        object.setLength(0);
        int capacity = object.capacity();
        int poolIndex = MathUtils.binaryLogarithm(capacity);

        if (poolIndex < 31) {
            _pool.get(poolIndex).returnObject(object);
        } else {
            _pool.get(30).returnObject(object);
        }
    }
}

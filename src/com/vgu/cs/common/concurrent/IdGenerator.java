package com.vgu.cs.common.concurrent;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 27/02/2021
 */

import java.util.concurrent.atomic.AtomicLong;

public class IdGenerator {
    private final AtomicLong _id;

    public IdGenerator(long initialId) {
        this._id = new AtomicLong(initialId);
    }

    public IdGenerator() {
        this(0L);
    }

    public long currentId() {
        return this._id.get();
    }

    public long nextId() {
        return this._id.incrementAndGet();
    }
}

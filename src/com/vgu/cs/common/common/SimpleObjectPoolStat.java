package com.vgu.cs.common.common;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/03/2021
 */

import java.util.concurrent.atomic.AtomicLong;

public class SimpleObjectPoolStat {

    private final AtomicLong nAlloc = new AtomicLong();
    private final AtomicLong nBorrow = new AtomicLong();
    private final AtomicLong nReturn = new AtomicLong();
    private final AtomicLong nInvalidate = new AtomicLong();
    private final AtomicLong nDestroy = new AtomicLong();
    private final AtomicLong nEvictUnused = new AtomicLong();
    private final AtomicLong nEvictExpired = new AtomicLong();

    public SimpleObjectPoolStat() {

    }

    private void clear() {
        nAlloc.set(0L);
        nBorrow.set(0L);
        nReturn.set(0L);
        nInvalidate.set(0L);
        nDestroy.set(0L);
        nEvictUnused.set(0L);
        nEvictExpired.set(0L);
    }

    public AtomicLong getNAlloc() {
        return nAlloc;
    }

    public AtomicLong getNBorrow() {
        return nBorrow;
    }

    public AtomicLong getNReturn() {
        return nReturn;
    }

    public AtomicLong getNInvalidate() {
        return nInvalidate;
    }

    public AtomicLong getNDestroy() {
        return nDestroy;
    }

    public AtomicLong getNEvictUnused() {
        return nEvictUnused;
    }

    public AtomicLong getNEvictExpired() {
        return nEvictExpired;
    }
}

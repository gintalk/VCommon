package com.vgu.cs.common.concurrent;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 27/02/2021
 */

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class VThreadFactory implements ThreadFactory {

    private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
    private final ThreadGroup _group;
    private final AtomicInteger _threadNumber = new AtomicInteger(1);
    private final String _namePrefix;

    public VThreadFactory(String name) {
        assert name != null;

        SecurityManager sm = System.getSecurityManager();
        this._group = sm != null ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this._namePrefix = name + "-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(this._group, r, this._namePrefix + this._threadNumber.getAndIncrement(), 0L);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }
        if (thread.getPriority() != 5) {
            thread.setPriority(5);
        }
        return thread;
    }
}

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vgu.cs.common.common;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author namnq
 */
public class SimpleObjectPool<T> {

    public enum ReturnResult {
        NULL,
        NULL_BY_DTOR,
        SUCCESS,
        EVICT_UNUSED,
        EVICT_EXPIRED
    }

    private final static List<SimpleObjectPool> POOL_LIST = new ArrayList<SimpleObjectPool>();
    private final static List<SimpleObjectPool> READ_ONLY_POOL_LIST = Collections.unmodifiableList(POOL_LIST);
    private final static ReentrantReadWriteLock POOL_LIST_LOCK = new ReentrantReadWriteLock();

    private final ArrayDeque<T> _deque;
    private final IConstructor<T> _constructor;
    private final IObjectBuilder<T> _objectBuilder;
    private final IDestructor<T> _destructor;

    private final String _name;
    private static final long DEFAULT_EVICT_THRESHOLD = 1000;
    private final long _evictThreshold;

    public SimpleObjectPool(String name, int initCap, IConstructor<T> constructor, IObjectBuilder<T> objectBuilder, IDestructor<T> destructor) {
        this(name, initCap, DEFAULT_EVICT_THRESHOLD, constructor, objectBuilder, destructor);
    }

    public SimpleObjectPool(String name, int initCap, long evictThreshold, IConstructor<T> constructor, IObjectBuilder<T> objectBuilder, IDestructor<T> destructor) {
        _deque = new ArrayDeque<>(initCap);
        _constructor = constructor;
        _objectBuilder = objectBuilder;
        _destructor = destructor;
        _evictThreshold = evictThreshold;
        _name = name;

        _addPoolToList();
    }

    private void _addPoolToList() {
        POOL_LIST_LOCK.writeLock().lock();

        try {
            POOL_LIST.add(this);
        } finally {
            POOL_LIST_LOCK.writeLock().unlock();
        }
    }

    public T borrowObject() {
        T object;
        IConstructor<T> constructor;
        IObjectBuilder<T> objectBuilder;
        boolean constructed = false;

        synchronized (this) {
            object = _deque.pollLast();
            objectBuilder = this._objectBuilder;
            constructor = this._constructor;
        }

        if (object != null) {
            if (objectBuilder != null) {
                object = objectBuilder.build(object);
            }
        } else if (constructor != null) {
            object = constructor.constructor();
            constructed = true;
        }

        return object;
    }

    public ReturnResult returnObject(T object) {
        if (object == null) {
            return ReturnResult.NULL;
        }

        IDestructor<T> destructor = _destructor;
        if (destructor != null) {
            object = destructor.destructor(object);

            if (object == null) {
                return ReturnResult.NULL_BY_DTOR;
            }
        }

        return ReturnResult.SUCCESS;
    }
}

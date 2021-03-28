package com.vgu.cs.common.common;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 25/03/2021
 */

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SimpleObjectPool<T> {
    private static final List<SimpleObjectPool> POOL_LIST = new ArrayList<>();
    private static final List<SimpleObjectPool> RO_POOL_LIST = new ArrayList<>();
    private static final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();
    private final SimpleObjectPoolStat _stat = new SimpleObjectPoolStat();
    private final AtomicLong _nContiguousBorrowFromPool = new AtomicLong();
    private final ArrayDeque<T> _queue;
    private final String _name;
    private final long _evictThreshold;
    private final IConstructor<T> _ctor;
    private IObjectBuilder<T> _obder;
    private IDestructor<T> _dtor;

    public SimpleObjectPool(String name, int initCap, long evictThreshold, IConstructor<T> ctor, IObjectBuilder<T> obder, IDestructor<T> dtor) {
        _name = name;
        _queue = new ArrayDeque<>(initCap);
        _evictThreshold = evictThreshold;
        _ctor = ctor;
        _obder = obder;
        _dtor = dtor;
        _addPoolToList();
    }

    public SimpleObjectPool(String name, int initCap, IConstructor<T> ctor, IObjectBuilder<T> obder) {
        _name = name;
        _queue = new ArrayDeque<>(initCap);
        _ctor = ctor;
        _obder = obder;
        _evictThreshold = 1000L;
        _addPoolToList();
    }

    public SimpleObjectPool(String name, int initCap, IConstructor<T> ctor) {
        _name = name;
        _queue = new ArrayDeque<>(initCap);
        _ctor = ctor;
        _evictThreshold = 1000L;
        _addPoolToList();
    }

    public SimpleObjectPool(String name, IConstructor<T> ctor) {
        _name = name;
        _queue = new ArrayDeque<>();
        _ctor = ctor;
        _evictThreshold = 1000L;
        _addPoolToList();
    }

    public T borrowObject() {
        boolean ctored = false;

        T ret;
        IConstructor<T> ctor;
        IObjectBuilder<T> obder;

        synchronized (this) {
            ret = _queue.pollLast();
            ctor = _ctor;
            obder = _obder;
        }

        if (ret != null) {
            if (obder != null) {
                ret = obder.build(ret);
            }
        } else if (ctor != null) {
            ret = ctor.ctor();
            ctored = true;
        }

        if (ret != null) {
            if (ctored) {
                _stat.getNAlloc().incrementAndGet();
                _nContiguousBorrowFromPool.set(0L);
            } else {
                _nContiguousBorrowFromPool.incrementAndGet();
            }

            _stat.getNBorrow().incrementAndGet();
        }

        return ret;
    }

    public T borrowObject(IMatcher<T> matcher, boolean autoCtor) {
        boolean ctored = false;

        T ret = null;
        IConstructor<T> ctor;
        IObjectBuilder<T> obder;

        synchronized (this) {
            Iterator<T> iterator = _queue.iterator();
            while (true) {
                if (iterator.hasNext()) {
                    T t = iterator.next();
                    if (!matcher.match(t)) {
                        continue;
                    }

                    ret = t;
                    iterator.remove();
                }

                obder = _obder;
                ctor = _ctor;

                break;
            }
        }

        if (ret != null) {
            if (obder != null) {
                ret = obder.build(ret);
            }
        } else if (ctor != null && autoCtor) {
            ret = ctor.ctor();
            ctored = true;
        }

        if (ret != null) {
            if (ctored) {
                _stat.getNAlloc().incrementAndGet();
                _nContiguousBorrowFromPool.set(0L);
            } else {
                _nContiguousBorrowFromPool.incrementAndGet();
            }

            _stat.getNBorrow().incrementAndGet();
        }

        return ret;
    }

    public T borrowObjectWoCtor() {
        T ret;
        IObjectBuilder<T> obder;

        synchronized (this) {
            if (_queue.isEmpty()) {
                return null;
            }

            ret = _queue.pollLast();
            obder = _obder;
        }

        if (ret != null && obder != null) {
            ret = obder.build(ret);
        }

        if (ret != null) {
            _nContiguousBorrowFromPool.incrementAndGet();
            _stat.getNBorrow().incrementAndGet();
        }

        return ret;
    }

    public void returnObject(T t) {
        if (t == null) {
            return;
        }

        IDestructor<T> dtor = _dtor;
        if (dtor != null) {
            t = dtor.dtor(t);
            if (t == null) {
                return;
            }
        }

        if (_stat.getNReturn().get() >= _stat.getNBorrow().get()) {
            _stat.getNEvictUnused().incrementAndGet();
            return;
        }
        if (_evictThreshold > 0L && _nContiguousBorrowFromPool.get() >= _evictThreshold) {
            _stat.getNEvictExpired().incrementAndGet();
            return;
        }

        synchronized (this) {
            _queue.addFirst(t);
        }
        _stat.getNReturn().incrementAndGet();
    }

    private void _addPoolToList() {
        LOCK.writeLock().lock();

        try {
            POOL_LIST.add(this);
        } finally {
            LOCK.writeLock().unlock();
        }
    }
}

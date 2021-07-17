package com.vgu.cs.common.server;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InstanceList<T> {

    private final List<WeakReference<T>> _instanceList;
    private final List<WeakReference<T>> _readOnlyList;
    private final ReentrantReadWriteLock _readWriteLock;

    public InstanceList() {
        this._instanceList = new ArrayList<>();
        this._readOnlyList = Collections.unmodifiableList(this._instanceList);
        this._readWriteLock = new ReentrantReadWriteLock();
    }

    public List<WeakReference<T>> getListForReading() {
        this._readWriteLock.readLock().lock();
        return this._readOnlyList;
    }

    public void releaseList() {
        this._readWriteLock.readLock().unlock();
    }

    public int size() {
        this._readWriteLock.readLock().lock();

        int size;
        try {
            size = this._instanceList.size();
        } finally {
            this._readWriteLock.readLock().unlock();
        }

        return size;
    }

    public void add(T instance) {
        boolean isNew = true;
        this._readWriteLock.writeLock().lock();

        try {
            int i = 0;
            for (int size = this._instanceList.size(); i < size; ++i) {
                T inst = this._instanceList.get(i).get();
                if (inst == null) {
                    this._instanceList.remove(i);
                    size = this._instanceList.size();
                    --i;
                } else if (inst == instance) {
                    isNew = false;
                }
            }

            if (isNew) {
                this._instanceList.add(new WeakReference<>(instance));
            }
        } finally {
            this._readWriteLock.writeLock().unlock();
        }
    }

    public void remove(T instance) {
        this._readWriteLock.writeLock().lock();

        try {
            int i = 0;
            for (int size = this._instanceList.size(); i < size; ++i) {
                T inst = this._instanceList.get(i).get();
                if (inst == null || inst == instance) {
                    this._instanceList.remove(i);
                    size = this._instanceList.size();
                    --i;
                }
            }
        } finally {
            this._readWriteLock.writeLock().unlock();
        }
    }

    public void cleanUp() {
        this._readWriteLock.writeLock().lock();

        try {
            int i = 0;
            for (int size = this._instanceList.size(); i < size; ++i) {
                T inst = this._instanceList.get(i).get();
                if (inst == null) {
                    this._instanceList.remove(i);
                    size = this._instanceList.size();
                    --i;
                }
            }
        } finally {
            this._readWriteLock.writeLock().unlock();
        }
    }
}

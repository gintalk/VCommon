package com.vgu.cs.common.thrift;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 03/03/2021
 */

import org.apache.commons.pool2.ObjectPool;
import org.apache.thrift.TServiceClient;

import java.util.NoSuchElementException;

public class TClientPool implements ObjectPool<TServiceClient> {

    @Override
    public void addObject() throws Exception, IllegalStateException, UnsupportedOperationException {

    }

    @Override
    public TServiceClient borrowObject() throws Exception, NoSuchElementException, IllegalStateException {
        return null;
    }

    @Override
    public void clear() throws Exception, UnsupportedOperationException {

    }

    @Override
    public void close() {

    }

    @Override
    public int getNumActive() {
        return 0;
    }

    @Override
    public int getNumIdle() {
        return 0;
    }

    @Override
    public void invalidateObject(TServiceClient obj) throws Exception {

    }

    @Override
    public void returnObject(TServiceClient obj) throws Exception {

    }
}

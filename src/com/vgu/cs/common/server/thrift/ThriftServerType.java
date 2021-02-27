package com.vgu.cs.common.server.thrift;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 25/02/2021
 */

public enum ThriftServerType {

    THREAD_POOL,
    HSHA,
    THREADED_SELECTOR;

    private ThriftServerType(){

    }
}

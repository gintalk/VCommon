package com.vgu.cs.common.thrift;

public enum ThriftServerType {

    THREAD_POOL,
    HSHA,
    THREADED_SELECTOR;

    private ThriftServerType(){

    }
}

package com.vgu.cs.common.server;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 25/02/2021
 */

import org.apache.thrift.server.TThreadedSelectorServer.Args.AcceptPolicy;

import java.util.Arrays;

public class ThriftServerConfiguration {

    public ThriftServerType serverType;
    public String host;
    public int port;
    public int ncoreThreads;
    public int nmaxThreads;
    public int maxWorkQueueSize;
    public boolean framed;
    public int maxFrameSize;
    public int totalFrameSize;
    public AcceptPolicy acceptPolicy;
    public int acceptQueueSizePerThread;
    public int nselectorThreads;

    public ThriftServerConfiguration(){
        this.serverType = ThriftServerType.THREADED_SELECTOR;
        this.host = "0.0.0.0";
        this.port = 1;
        this.ncoreThreads = 16;
        this.nmaxThreads = this.ncoreThreads * 2;
        this.maxWorkQueueSize = 0;
        this.framed = true;
        this.maxFrameSize = 65536;
        this.totalFrameSize = 1073741824;
        this.acceptPolicy = AcceptPolicy.FAST_ACCEPT;
        this.acceptQueueSizePerThread = 64;
        this.nselectorThreads = 8;
    }

    public ThriftServerConfiguration clone() {
        ThriftServerConfiguration ret = new ThriftServerConfiguration();
        ret.serverType = this.serverType;
        ret.host = this.host;
        ret.port = this.port;
        ret.ncoreThreads = this.ncoreThreads;
        ret.nmaxThreads = this.nmaxThreads;
        ret.maxWorkQueueSize = this.maxWorkQueueSize;
        ret.framed = this.framed;
        ret.maxFrameSize = this.maxFrameSize;
        ret.totalFrameSize = this.totalFrameSize;
        ret.acceptPolicy = this.acceptPolicy;
        ret.acceptQueueSizePerThread = this.acceptQueueSizePerThread;
        ret.nselectorThreads = this.nselectorThreads;

        return ret;
    }

    public boolean isValid() {
        boolean valid = this.serverType == ThriftServerType.HSHA || this.serverType == ThriftServerType.THREADED_SELECTOR || this.serverType == ThriftServerType.THREAD_POOL;
        valid = valid && !this.host.isEmpty() && this.port > 0 && this.ncoreThreads > 0 && this.nmaxThreads >= this.ncoreThreads;
        if (this.serverType == ThriftServerType.THREAD_POOL && this.framed) {
            valid = valid && this.maxFrameSize > 0;
        }

        if (this.serverType == ThriftServerType.HSHA || this.serverType == ThriftServerType.THREADED_SELECTOR) {
            valid = valid && this.framed && this.maxFrameSize > 0 && this.totalFrameSize >= this.maxFrameSize;
        }

        if (this.serverType == ThriftServerType.THREADED_SELECTOR) {
            valid = valid && this.acceptQueueSizePerThread > 0 && this.nselectorThreads > 0 && (this.acceptPolicy == AcceptPolicy.FAIR_ACCEPT || this.acceptPolicy == AcceptPolicy.FAST_ACCEPT);
        }

        return valid;
    }
}

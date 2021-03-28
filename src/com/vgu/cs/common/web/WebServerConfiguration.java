package com.vgu.cs.common.web;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/03/2021
 */

public class WebServerConfiguration {

    public String host = "0.0.0.0";
    public int port = 80;
    public int nConnectors = 1;
    public int nAcceptors = 2;
    public int nSelectors = 4;
    public int acceptQueueSize = 500;
    public int nMinThreads = 100;
    public int nMaxThreads;
    public int maxIdleTime;
    public int connMaxIdleTime;
    public int threadMaxIdleTime;

    public WebServerConfiguration() {
        nMaxThreads = nMinThreads * 2;
        maxIdleTime = 5000;
        connMaxIdleTime = maxIdleTime;
        threadMaxIdleTime = maxIdleTime;
    }

    public WebServerConfiguration clone() {
        WebServerConfiguration ret = new WebServerConfiguration();
        ret.host = host;
        ret.port = port;
        ret.nConnectors = nConnectors;
        ret.nAcceptors = nAcceptors;
        ret.nSelectors = nSelectors;
        ret.acceptQueueSize = acceptQueueSize;
        ret.nMinThreads = nMinThreads;
        ret.nMaxThreads = nMaxThreads;
        ret.maxIdleTime = maxIdleTime;
        ret.connMaxIdleTime = connMaxIdleTime;
        ret.threadMaxIdleTime = threadMaxIdleTime;

        return ret;
    }

    public boolean isValid() {
        return !host.isEmpty() && port > 0 && nConnectors > 0 && nAcceptors > 0 && nSelectors > 0 && acceptQueueSize > 0 && nMinThreads > 0 && nMaxThreads >= nMinThreads && connMaxIdleTime > 0 && threadMaxIdleTime > 0;
    }
}

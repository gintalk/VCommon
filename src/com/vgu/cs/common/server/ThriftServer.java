package com.vgu.cs.common.server;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 25/02/2021
 */

import com.vgu.cs.common.concurrent.VThreadFactory;
import com.vgu.cs.common.config.VConfig;
import com.vgu.cs.common.exception.InvalidParameterException;
import com.vgu.cs.common.logger.VLogger;
import com.vgu.cs.common.util.NumberUtils;
import com.vgu.cs.common.util.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.server.TThreadedSelectorServer.Args.AcceptPolicy;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThriftServer {

    public static final InstanceList<ThriftServer> INSTANCES = new InstanceList<>();
    private static final Logger LOGGER = VLogger.getLogger(ThriftServer.class);
    private final ThriftServerConfiguration _configuration;
    private final String _name;
    private ThreadPoolExecutor _executor;
    private TServer _server;
    private Class<? extends TServer> _serverClass;
    private String _info;

    public ThriftServer(String name) {
        this._configuration = new ThriftServerConfiguration();
        if (StringUtils.isNullOrEmpty(name)) {
            this._name = "main";
        } else {
            this._name = name.trim();
        }
        this._serverClass = null;
        this._info = null;
    }

    public boolean registerProcessor(TProcessor processor) {
        if (this._server != null) {
            System.out.println("[WARN] Server has already been setup");
            return true;
        }

        try {
            assert processor != null;

            this._configuration.serverType = VConfig.INSTANCE.getEnum(ThriftServer.class, ThriftServerType.class, "serverType", this._configuration.serverType);
            this._configuration.host = VConfig.INSTANCE.getString(ThriftServer.class, "host", this._configuration.host);
            this._configuration.port = VConfig.INSTANCE.getInt(ThriftServer.class, "port", this._configuration.port);
            this._configuration.ncoreThreads = VConfig.INSTANCE.getInt(ThriftServer.class, "ncoreThreads", this._configuration.ncoreThreads);
            this._configuration.nmaxThreads = VConfig.INSTANCE.getInt(ThriftServer.class, "nmaxThreads", this._configuration.nmaxThreads);
            this._configuration.maxWorkQueueSize = VConfig.INSTANCE.getInt(ThriftServer.class, "maxWorkQueueSize", this._configuration.maxWorkQueueSize);

            if (this._configuration.serverType == ThriftServerType.HSHA) {
                this.setupHsHaServer(processor);
            } else if (this._configuration.serverType == ThriftServerType.THREAD_POOL) {
                this.setupThreadPoolServer(processor);
            } else if (this._configuration.serverType == ThriftServerType.THREADED_SELECTOR) {
                this.setupThreadedSelectorServer(processor);
            } else {
                throw new InvalidParameterException("Unsupported server type");
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
    }

    private void setupHsHaServer(TProcessor processor) throws TTransportException, UnknownHostException {
        this._configuration.maxFrameSize = VConfig.INSTANCE.getInt(ThriftServer.class, "maxFrameSize", this._configuration.maxFrameSize);
        this._configuration.totalFrameSize = VConfig.INSTANCE.getInt(ThriftServer.class, "totalFrameSize", this._configuration.totalFrameSize);

        assert this._configuration.isValid();

        InetAddress inetAddress = InetAddress.getByName(this._configuration.host);
        TNonblockingServerSocket socket = new TNonblockingServerSocket(this._configuration.port);
        THsHaServer.Args args = new THsHaServer.Args(socket);

        LinkedBlockingQueue<Runnable> workQueue;
        if (this._configuration.maxWorkQueueSize > 0) {
            workQueue = new LinkedBlockingQueue<>(this._configuration.maxWorkQueueSize);
        } else {
            workQueue = new LinkedBlockingQueue<>();
        }

        int stopTimeoutVal = args.getStopTimeoutVal();
        TimeUnit stopTimeoutUnit = args.getStopTimeoutUnit();
        this._executor = new ThreadPoolExecutor(
                this._configuration.ncoreThreads,
                this._configuration.nmaxThreads,
                stopTimeoutVal,
                stopTimeoutUnit,
                workQueue,
                new VThreadFactory(this._name)
        );

        args.executorService(this._executor);
        args.processor(processor);
        args.maxReadBufferBytes = this._configuration.maxFrameSize;
        args.transportFactory(new TFramedTransport.Factory(this._configuration.maxFrameSize));
        args.protocolFactory(new TBinaryProtocol.Factory(false, true));

        this._server = new THsHaServer(args);
        LOGGER.info(String.format("Thrift Server (THsHaServer-%s) listens on %s:%s", this._name, inetAddress.getHostAddress(), this._configuration.port));

        this._serverClass = this._server.getClass();
        this._info = "host=" + inetAddress.getHostAddress() + ", port=" + this._configuration.port + ", "
                + args.getClass().getName() + "{" + this._executor.getThreadFactory().getClass().getName() + ", "
                + this._executor.getQueue().getClass().getName()
                + "{capacity=" + (this._configuration.maxWorkQueueSize > 0 ? NumberUtils.formatNumber(this._configuration.maxWorkQueueSize) : "unlimited")
                + "}}, framed=true, protocol=binary, maxFrameSize=" + NumberUtils.formatNumber(this._configuration.maxFrameSize)
                + ", totalFrameSize=" + NumberUtils.formatNumber(this._configuration.totalFrameSize) + "}"
        ;

        INSTANCES.add(this);
    }

    private void setupThreadPoolServer(TProcessor processor) throws TTransportException, UnknownHostException {
        this._configuration.framed = VConfig.INSTANCE.getBoolean(ThriftServer.class, "framed", this._configuration.framed);
        this._configuration.maxFrameSize = VConfig.INSTANCE.getInt(ThriftServer.class, "maxFrameSize", this._configuration.maxFrameSize);

        assert this._configuration.isValid();

        InetAddress inetAddress = InetAddress.getByName(this._configuration.host);
        TServerSocket socket = new TServerSocket(this._configuration.port);
        TThreadPoolServer.Args args = new TThreadPoolServer.Args(socket);

        LinkedBlockingQueue<Runnable> workQueue;
        if (this._configuration.maxWorkQueueSize > 0) {
            workQueue = new LinkedBlockingQueue<>(this._configuration.maxWorkQueueSize);
        } else {
            workQueue = new LinkedBlockingQueue<>();
        }

        int stopTimeoutVal = args.stopTimeoutVal;
        TimeUnit stopTimeoutUnit = args.stopTimeoutUnit;
        this._executor = new ThreadPoolExecutor(
                this._configuration.ncoreThreads,
                this._configuration.nmaxThreads,
                stopTimeoutVal,
                stopTimeoutUnit,
                workQueue,
                new VThreadFactory(this._name)
        );

        args.executorService(this._executor);
        args.processor(processor);
        if (this._configuration.framed) {
            args.transportFactory(new TFramedTransport.Factory(this._configuration.maxFrameSize));
        }
        args.protocolFactory(new org.apache.thrift.protocol.TBinaryProtocol.Factory(false, true));

        this._server = new TThreadPoolServer(args);
        LOGGER.info(String.format("Thrift server (TThreadPoolServer-%s) listens on %s:%s", this._name, inetAddress.getHostAddress(), this._configuration.port));

        this._serverClass = this._server.getClass();
        this._info = "host=" + inetAddress.getHostAddress() + ", port=" + this._configuration.port + ", "
                + args.getClass().getName() + "{" + this._executor.getClass().getName() + "{"
                + this._executor.getThreadFactory().getClass().getName() + ", " + this._executor.getQueue().getClass().getName()
                + "{capacity=" + (this._configuration.maxWorkQueueSize > 0 ? NumberUtils.formatNumber(this._configuration.maxWorkQueueSize) : "unlimited")
                + "}}, framed=" + this._configuration.framed + ", protocol=binary, maxFrameSize=" + NumberUtils.formatNumber(this._configuration.maxFrameSize) + "}"
        ;

        INSTANCES.add(this);
    }

    private void setupThreadedSelectorServer(TProcessor processor) throws TTransportException, UnknownHostException {
        this._configuration.maxFrameSize = VConfig.INSTANCE.getInt(ThriftServer.class, "maxFrameSize", this._configuration.maxFrameSize);
        this._configuration.totalFrameSize = VConfig.INSTANCE.getInt(ThriftServer.class, "totalFrameSize", this._configuration.totalFrameSize);
        this._configuration.acceptPolicy = VConfig.INSTANCE.getEnum(ThriftServer.class, AcceptPolicy.class, "acceptPolicy", this._configuration.acceptPolicy);
        this._configuration.acceptQueueSizePerThread = VConfig.INSTANCE.getInt(ThriftServer.class, "acceptQueueSizePerThread", this._configuration.acceptQueueSizePerThread);
        this._configuration.nselectorThreads = VConfig.INSTANCE.getInt(ThriftServer.class, "nselectorThreads", this._configuration.nselectorThreads);

        assert this._configuration.isValid();

        InetAddress inetAddress = InetAddress.getByName(this._configuration.host);
        TNonblockingServerSocket socket = new TNonblockingServerSocket(this._configuration.port);
        org.apache.thrift.server.TThreadedSelectorServer.Args options = new org.apache.thrift.server.TThreadedSelectorServer.Args(socket);

        LinkedBlockingQueue<Runnable> workQueue;
        if (this._configuration.maxWorkQueueSize > 0) {
            workQueue = new LinkedBlockingQueue<>(this._configuration.maxWorkQueueSize);
        } else {
            workQueue = new LinkedBlockingQueue<>();
        }

        int stopTimeoutVal = options.getStopTimeoutVal();
        TimeUnit stopTimeoutUnit = options.getStopTimeoutUnit();
        this._executor = new ThreadPoolExecutor(this._configuration.ncoreThreads, this._configuration.nmaxThreads, stopTimeoutVal, stopTimeoutUnit, workQueue, new VThreadFactory(this._name));

        options.executorService(this._executor);
        options.processor(processor);
        options.maxReadBufferBytes = this._configuration.maxFrameSize;
        options.transportFactory(new TFramedTransport.Factory(this._configuration.maxFrameSize));
        options.protocolFactory(new org.apache.thrift.protocol.TBinaryProtocol.Factory(false, true));
        options.acceptPolicy(this._configuration.acceptPolicy);
        options.acceptQueueSizePerThread(this._configuration.acceptQueueSizePerThread);
        options.selectorThreads = this._configuration.nselectorThreads;

        this._server = new TThreadedSelectorServer(options);
        LOGGER.info(String.format("Thrift server (TThreadedSelectorServer-%s) listens on %s:%s", this._name, inetAddress.getHostAddress(), this._configuration.port));

        this._serverClass = this._server.getClass();
        this._info = "host=" + inetAddress.getHostAddress() + ", port=" + this._configuration.port + ", "
                + options.getClass().getName() + "{" + this._executor.getClass().getName()
                + "{" + this._executor.getThreadFactory().getClass().getName() + ", " + this._executor.getQueue().getClass().getName()
                + "{capacity=" + (this._configuration.maxWorkQueueSize > 0 ? NumberUtils.formatNumber(this._configuration.maxWorkQueueSize) : "unlimited")
                + "}}, framed=true, protocol=binary, maxFrameSize=" + NumberUtils.formatNumber(this._configuration.maxFrameSize)
                + ", totalFrameSize=" + NumberUtils.formatNumber(this._configuration.totalFrameSize) + ", acceptPolicy=" + this._configuration.acceptPolicy
                + ", acceptQueueSizePerThread=" + NumberUtils.formatNumber(this._configuration.acceptQueueSizePerThread)
                + ", selectorThreads=" + NumberUtils.formatNumber(this._configuration.nselectorThreads) + "}"
        ;

        INSTANCES.add(this);
    }
}

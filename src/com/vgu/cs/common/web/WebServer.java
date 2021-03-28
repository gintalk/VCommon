package com.vgu.cs.common.web;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 25/03/2021
 */

import com.vgu.cs.common.config.VConfig;
import com.vgu.cs.common.logger.VLogger;
import com.vgu.cs.common.server.InstanceList;
import com.vgu.cs.common.util.StringUtils;
import com.vgu.cs.common.util.VUtils;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.concurrent.atomic.AtomicBoolean;

public class WebServer {

    public static final InstanceList<WebServer> INSTANCES = new InstanceList<>();
    private static final Logger LOGGER = VLogger.getLogger(WebServer.class);
    private final AtomicBoolean _isRunning = new AtomicBoolean(false);
    private final WebServerConfiguration _configuration;
    private final String _name;
    private Server _server;
    private QueuedThreadPool _threadPool;
    private Thread _thread;

    public WebServer(String name) {
        _configuration = new WebServerConfiguration();
        if (StringUtils.isNullOrEmpty(name)) {
            _name = "main";
        } else {
            _name = name.trim();
        }
        _threadPool = null;
    }

    public boolean setup(Handler handler) {
        if (_server != null) {
            LOGGER.warn("System has already been setup");
            return true;
        }

        try {
            _configuration.host = VConfig.INSTANCE.getString(WebServer.class, "host", _configuration.host);
            _configuration.port = VConfig.INSTANCE.getInt(WebServer.class, "port", _configuration.port);
            _configuration.nConnectors = VConfig.INSTANCE.getInt(WebServer.class, "nConnectors", _configuration.nConnectors);
            _configuration.nAcceptors = VConfig.INSTANCE.getInt(WebServer.class, "nAcceptors", _configuration.nAcceptors);
            _configuration.nSelectors = VConfig.INSTANCE.getInt(WebServer.class, "nSelectors", _configuration.nSelectors);
            _configuration.acceptQueueSize = VConfig.INSTANCE.getInt(WebServer.class, "acceptQueueSize", _configuration.acceptQueueSize);
            _configuration.nMinThreads = VConfig.INSTANCE.getInt(WebServer.class, "nMinThreads", _configuration.nMinThreads);
            _configuration.nMaxThreads = VConfig.INSTANCE.getInt(WebServer.class, "nMaxThreads", _configuration.nMaxThreads * 2);
            _configuration.maxIdleTime = VConfig.INSTANCE.getInt(WebServer.class, "maxIdleTime", _configuration.maxIdleTime);
            _configuration.connMaxIdleTime = VConfig.INSTANCE.getInt(WebServer.class, "connMaxIdleTime", _configuration.maxIdleTime);
            _configuration.threadMaxIdleTime = VConfig.INSTANCE.getInt(WebServer.class, "threadMaxIdleTime", _configuration.maxIdleTime);

            if (!_configuration.isValid()) {
                LOGGER.error("Invalid configuration");
                return false;
            }

            _threadPool = new QueuedThreadPool();
            _threadPool.setName(_name);
            _threadPool.setMinThreads(_configuration.nMinThreads);
            _threadPool.setMaxThreads(_configuration.nMaxThreads);
            _threadPool.setIdleTimeout(_configuration.threadMaxIdleTime);

            Server server = new Server(_threadPool);
            ServerConnector[] connectors = new ServerConnector[_configuration.nConnectors];

            for (int i = 0; i < _configuration.nConnectors; i++) {
                ServerConnector connector = new ServerConnector(server, _configuration.nAcceptors, _configuration.nSelectors);

                connector.setHost(_configuration.host);
                connector.setPort(_configuration.port);
                connector.setIdleTimeout(_configuration.connMaxIdleTime);
                connector.setAcceptQueueSize(_configuration.acceptQueueSize);

                connectors[i] = connector;
            }

            server.setConnectors(connectors);
            server.setStopAtShutdown(true);
            if (handler != null) {
                server.setHandler(handler);
            }

            _server = server;
            INSTANCES.add(this);

            return true;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return false;
        }
    }

    public boolean start() {
        if (_server == null) {
            return false;
        }
        if (_isRunning.compareAndSet(false, true)) {
            LOGGER.warn("Server is already running");
            return true;
        }

        boolean ret = false;
        try {
            _server.start();
            _thread = new Thread(new WebServer.ServerRunner(_server, _isRunning), this.getClass().getSimpleName() + "-" + _name + VUtils.nextGID());
            _thread.start();

            ret = true;
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            stop();
        }

        return ret;
    }

    public void stop() {
        if (_server == null) {
            return;
        }
        if (!_isRunning.get()) {
            return;
        }

        try {
            _server.stop();
            if (_thread != null) {
                _thread.join();
                _thread = null;
            } else {
                _isRunning.set(false);
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private static class ServerRunner implements Runnable {
        private final Server _server;
        private final AtomicBoolean _isRunning;

        public ServerRunner(Server server, AtomicBoolean isRunning) {
            _server = server;
            _isRunning = isRunning;
        }

        @Override
        public void run() {
            WebServer.LOGGER.info("Web server is going to server");

            try {
                _server.join();
            } catch (Exception ex) {
                WebServer.LOGGER.error(ex.getMessage(), ex);
            }

            WebServer.LOGGER.info("Web server stopped");
            _isRunning.set(false);
        }
    }
}

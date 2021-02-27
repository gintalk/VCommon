package com.vgu.cs.common.server;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 25/02/2021
 */

import com.vgu.cs.common.config.VConfig;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;

public class ThriftServer {

    private final ThriftServerConfiguration _configuration = new ThriftServerConfiguration();
    private TServer _server;

    public boolean registerProcessor(TProcessor processor) {
        if (this._server != null) {
            System.out.println("[WARN] Server has already been setup");
            return true;
        }

        try {
            assert processor != null;

            this._configuration.serverType = VConfig.INSTANCE.getEnum(ThriftServer.class, ThriftServerType.class, "serverType", this._configuration.serverType);
            
        }
    }
}

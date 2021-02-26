package com.vgu.cs.common.server;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 25/02/2021
 */

import org.apache.logging.log4j.core.Logger;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;

public class ThriftServer {

    private TServer _server;
    private final ThriftServerConfiguration _configuration = new ThriftServerConfiguration();

//    public boolean registerProcessor(TProcessor processor){
//        if(this._server != null){
//            System.out.println("[WARN] Server has already been setup");
//            return true;
//        }
//
//        try{
//            assert processor != null;
//
//        }
//    }
//
//    private <T extends Enum<T>> T readEnumConfig(Class<T> enumType, String key, T defaultValue){
//
//    }
}

package com.vgu.cs.common.thrift;

import com.vgu.cs.common.common.thrift.TErrorCode;
import com.vgu.cs.common.logger.VLogger;
import org.apache.logging.log4j.Logger;

import java.util.function.IntFunction;

public class TBaseHandler {

    private static final Logger LOGGER = VLogger.getLogger(TBaseHandler.class);


    public TBaseHandler() {
    }

    protected void ping() {
        LOGGER.info("ping");
    }

    protected <T> T process(IntFunction<T> function) {
        return _doProcess(function);
    }

    private <T> T _doProcess(IntFunction<T> function) {
        try {
            return function.apply(TErrorCode.SUCCESS.getValue());
        } catch (Throwable e) {
            LOGGER.error(e.getMessage(), e);
            return function.apply(TErrorCode.FAIL.getValue());
        }
    }

    protected void process(Runnable function) {
        _doProcess(function);
    }

    private void _doProcess(Runnable function) {
        try {
            function.run();
        } catch (Throwable t) {
            LOGGER.error(t.getMessage(), t);
            function.run();
        }
    }
}

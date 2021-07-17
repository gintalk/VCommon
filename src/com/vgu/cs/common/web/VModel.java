package com.vgu.cs.common.web;

import com.vgu.cs.common.logger.VLogger;
import com.vgu.cs.common.web.thrift.TStatusCode;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class VModel<T extends VRequest> {

    private static final Logger LOGGER = VLogger.getLogger(VModel.class);
    private final Map<String, Method> METHOD_MAP;
    private final Class<T> CLAZZ;

    public VModel(Class<T> clazz, String container, String group, String version) {
        METHOD_MAP = new ConcurrentHashMap<>();
        CLAZZ = clazz;
        VModelController.INSTANCE.registerModel(this, container, group, version);
    }

    public VResponse doProcess(VRequest req) {
        try {
            VModel<T> model = VModelController.INSTANCE.getModel(
                    req.getPath().detail.getContainer(),
                    req.getPath().detail.getGroup(),
                    req.getPath().detail.getVersion()
            );
            if (model == null) {
                return new VResponse(TStatusCode.FAIL);
            }

            String methodName = req.getPath().detail.getMethodName();
            String methodKey = req.getPath().detail.getVersion() + "." + methodName;

            Method method;
            if (METHOD_MAP.containsKey(methodKey)) {
                method = METHOD_MAP.get(methodKey);
            } else {
                method = model.getClass().getMethod(methodName, CLAZZ);
                method.setAccessible(true);
                METHOD_MAP.put(methodKey, method);
            }

            return (VResponse) method.invoke(model, req);
        } catch (Exception ex){
            LOGGER.error(ex.getMessage(), ex);
            return new VResponse(TStatusCode.FAIL);
        }
    }

    public abstract T buildRequest(HttpServletRequest req, HttpServletResponse res, VPath path);
}

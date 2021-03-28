package com.vgu.cs.common.web;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 26/03/2021
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class VModel<T extends IRequest> {

    private final Map<String, Method> METHOD_MAP;
    private final Class<T> CLAZZ;

    public VModel(Class<T> clazz, String container, String group) {
        METHOD_MAP = new ConcurrentHashMap<>();
        CLAZZ = clazz;
        _register(container, group);
    }

    public abstract T buildRequest(HttpServletRequest req, HttpServletResponse res, VPath path);

    private void _register(String container, String group) {
        ModelController.INSTANCE.register(this, "V1", container, group);
    }

    public static class ModelController {

        public static final ModelController INSTANCE = new ModelController();
        private final Map<String, VModel<?>> MODEL_BY_CONTAINER;

        private ModelController() {
            MODEL_BY_CONTAINER = new HashMap<>();
        }

        public synchronized void register(VModel<?> model, String version, String container, String group) {
            MODEL_BY_CONTAINER.put(_buildKey(version, container, group), model);
        }

        private String _buildKey(String version, String container, String group) {
            return String.format("%s.%s.%s", version.toUpperCase(), container.toUpperCase(), group.toUpperCase());
        }
    }
}

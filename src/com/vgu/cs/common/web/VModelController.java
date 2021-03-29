package com.vgu.cs.common.web;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 28/03/2021
 */

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VModelController {
    public static final VModelController INSTANCE = new VModelController();
    private final Map<String, VModel<? extends VRequest>> MODEL_MAP;

    private VModelController() {
        MODEL_MAP = new ConcurrentHashMap<>();
    }

    public <T extends VRequest> void registerModel(VModel<T> model, String container, String group, String version) {
        MODEL_MAP.put(_buildKey(container, group, version), model);
    }

    @SuppressWarnings("unchecked")
    public <T extends VRequest> VModel<T> getModel(String container, String group, String version) {
        return (VModel<T>) MODEL_MAP.get(_buildKey(container, group, version));
    }

    private String _buildKey(String container, String group, String version) {
        return String.format("%s.%s.%s", container, group, version);
    }
}

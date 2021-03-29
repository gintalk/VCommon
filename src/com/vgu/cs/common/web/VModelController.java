package com.vgu.cs.common.web;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 28/03/2021
 */

import java.util.HashMap;
import java.util.Map;

public class VModelController {
    public static final VModelController INSTANCE = new VModelController();
    private final Map<String, VModel<? extends IRequest>> MODEL_BY_CONTAINER;

    private VModelController() {
        MODEL_BY_CONTAINER = new HashMap<>();
    }

    public synchronized <T extends IRequest> void register(VModel<T> model, String version, String container, String group) {
        MODEL_BY_CONTAINER.put(_buildKey(version, container, group), model);
    }


    @SuppressWarnings("unchecked")
    public <T extends IRequest> VModel<T> getModel(VPath.Detail pathDetail) {
        return (VModel<T>) MODEL_BY_CONTAINER.get(_buildKey(pathDetail.getVersion(), pathDetail.getContainer(), pathDetail.getGroup()));
    }

    private String _buildKey(String version, String container, String group) {
        return String.format("%s.%s.%s", version.toUpperCase(), container.toUpperCase(), group.toUpperCase());
    }
}

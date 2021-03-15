package com.vgu.cs.common.util;

/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 *
 * @author namnh16 on 03/03/2021
 */

import java.util.List;
import java.util.Map;

public class CollectionUtils {

    public static boolean isNullOrEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isNullOrEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static boolean isNullOrEmpty(Map<?, ?> map){
        return map == null || map.isEmpty();
    }
}

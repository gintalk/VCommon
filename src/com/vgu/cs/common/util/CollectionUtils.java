package com.vgu.cs.common.util;

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

package com.vgu.cs.common.util;

public class ConvertUtils {
    
    public static int toInteger(Object in) {
        return toInteger(in, 0);
    }
    
    public static int toInteger(Object in, int def) {
        return _to(Integer.TYPE, in, def);
    }
    
    public static long toLong(Object obj) {
        return toLong(obj, 0L);
    }
    
    public static long toLong(Object obj, long def) {
        return _to(Long.TYPE, obj, def);
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private static <T> T _to(Class<T> to, Object from, T def) {
        try {
            if (from == null) {
                return def;
            }
            
            if (to == Integer.TYPE) {
                return (T) Integer.valueOf(from.toString());
            } else if (to == Long.TYPE) {
                return (T) Long.valueOf(from.toString());
            } else {
                return def;
            }
        } catch (NumberFormatException ex) {
            return def;
        }
    }
}

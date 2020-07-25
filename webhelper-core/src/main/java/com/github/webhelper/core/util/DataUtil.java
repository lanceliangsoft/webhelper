package com.github.webhelper.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {

    public static List<String> addList(Object... items) {
        List<String> list = new ArrayList<>();
        for (Object item : items) {
            if (item instanceof String) {
                list.add((String) item);
            } else if (item instanceof Collection) {
                Collection<?> collection = (Collection<?>) item;
                for (Object obj : collection) {
                    if (obj instanceof String) {
                        list.add((String) obj);
                    }
                }
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> asMap(K k1, V v1, Object... otherArgs) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        if (otherArgs != null) {
            for(int i=0; i<otherArgs.length; i+=2){
                map.put((K) otherArgs[i], (V) otherArgs[i+1]);
            }
        }
        return map;
    }

    public static <T> T eitherOf(T... objs) {
        for (T obj : objs) {
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    public static <T> T eitherOf(T[]... arrays) {
        for (T[] array : arrays) {
            if (array != null && array.length > 0) {
                return array[0];
            }
        }
        return null;
    }
}

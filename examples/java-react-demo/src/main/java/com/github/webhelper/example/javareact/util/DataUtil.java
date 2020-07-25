package com.github.webhelper.example.javareact.util;

import java.util.HashMap;
import java.util.Map;

public class DataUtil {
    private DataUtil() {}

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
}

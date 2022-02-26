package com.spiderwalker.chance.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {
    public static Map<String, Object> initOptions(Map<String, Object> options, Map<String, Object> defaults) {
        if (options == null) {
            options = new HashMap<>();
        }
        if (defaults != null) {
            for (String key : defaults.keySet()) {
                if (options.get(key) == null) {
                    options.put(key, defaults.get(key));
                }
            }
        }

        return options;
    }

    public static <T> T get(Map<String, Object> options, String key) {
        return (T) options.get(key);
    }

    public static boolean isExist(Map<String, Object> options, String key) {
        return options.get(key) != null;
    }
}

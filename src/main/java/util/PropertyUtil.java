package util;

import java.util.*;

public class PropertyUtil {

    static final String DILIMETER_OF_POINTER = ".";

    /**
     * recurseCreateMaps
     *
     * @param currentMap
     * @param key
     * @param value
     * */
    public static void recurseCreateMaps(Map<String, Object> currentMap, String key, String value) {
        if (key.contains(DILIMETER_OF_POINTER)) {
            String currentKey = key.split("\\.")[0];
            Map<String, Object> deeperMap;
            if (currentMap.get(currentKey) instanceof Map) {
                deeperMap = (Map<String, Object>) currentMap.get(currentKey);
            } else {
                deeperMap = new HashMap<>();
                currentMap.put(currentKey, deeperMap);
            }
            recurseCreateMaps(deeperMap, key.substring(key.indexOf(DILIMETER_OF_POINTER) + 1), value);
        } else {
            currentMap.put(key, value);
        }
    }

}

package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PropertiesUtil {

    static final String DILIMETER_OF_POINTER = ".";

    /**
     * getPropertiseMapFromFile
     *
     * @param file - Properties 파일 읽기
     * */
    public static Map<String, String> readPropertiseFile(File file) throws IOException {
        Properties properties = new Properties();
        properties.load(new BufferedReader(new FileReader(file)));
        return (Map) properties;
    }

    /**
     * recurseCreateLists
     *
     * @param accumulateMap - 누적되는 Map 자료구조
     * @param properties
     * @param resIdx
     * */
    public static void recurseCreateLists(Map<String, List<String>> accumulateMap, Map<String, String> properties, int resIdx) {
        for(Map.Entry<String, String> entry : properties.entrySet()) {
            List<String> values = accumulateMap.get(entry.getKey());
            if(values == null) {
                values = new LinkedList<>();
                accumulateMap.put(entry.getKey(), values);
                if(resIdx != 0) {
                    for(int i=0; i<resIdx; i++) {
                        values.add("");
                    }
                }
            }
            values.add(entry.getValue());
        }
    }

    /**
     * recurseCreateMaps
     *
     * @param currentMap - 누적되는 Map 자료구조
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

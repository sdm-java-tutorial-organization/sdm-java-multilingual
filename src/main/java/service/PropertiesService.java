package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PropertiesService {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesService.class);

    private static PropertiesService instance = new PropertiesService();

    private PropertiesService() {
        logger.debug("create PropertiesService");
    }

    public static PropertiesService getInstance() {
        return instance;
    }

    public Map<String, String> getPropertiseMapFromFile(File file) throws IOException {
        Properties properties = new Properties();
        properties.load(new BufferedReader(new FileReader(file)));
        return (Map) properties;
    }

    public void accumulateExcel(Map<String, List<String>> result,
                                Map<String, String> properties,
                                int index) {
        for(Map.Entry<String, String> entry : properties.entrySet()) {
            List<String> listOfValue = result.get(entry.getKey());
            if(listOfValue == null) {
                listOfValue = new LinkedList<>();
                result.put(entry.getKey(), listOfValue);
                if(index != 0) {
                    for(int i=0; i<index; i++) {
                        listOfValue.add("");
                    }
                }
            }
            listOfValue.add(entry.getValue());
        }
    }

}

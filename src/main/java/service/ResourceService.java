package service;

import model.Document;
import model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);

    private static ResourceService instance = new ResourceService();

    private ResourceService() {
        logger.debug("create ResourceService");
    }

    public static ResourceService getInstance() {
        return instance;
    }

    /**
     * convertDocumentToResources
     *
     * @param document
     * */
    public List<Resource> convertDocumentToResources(Document document) {
        List<Resource> resources = new ArrayList<>();
        final int COUNT_OF_RESOURCE = document.getLengthX() - 1;
        List<String> keys = new ArrayList<>(document.getBody().keySet());
        List<List<String>> listOfValues = new ArrayList<>(document.getBody().values());

        // Empty
        for(int resIdx=0; resIdx<COUNT_OF_RESOURCE; resIdx++) {
            String title = document.getHeader().get(resIdx + 1);
            Map<String, String> properties = new HashMap<>();
            Resource resource = new Resource(title, properties);
            resources.add(resource);
        }

        // insert properties
        for(int row=0; row<keys.size(); row++) {
            for(int column=0; column<COUNT_OF_RESOURCE; column++) {
                resources.get(column).getProperties().put(keys.get(row), listOfValues.get(row).get(column));
            }
        }

        return resources;
    }

}
